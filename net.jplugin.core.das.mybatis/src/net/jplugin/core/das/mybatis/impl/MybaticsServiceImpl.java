package net.jplugin.core.das.mybatis.impl;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.api.DataSourceHolder;
import net.jplugin.core.das.mybatis.api.IMapperHandlerForReturn;
import net.jplugin.core.service.api.ServiceFactory;

public class MybaticsServiceImpl implements IMybatisService {
	TxManagedDataSource managedDataSource=null;
	SqlSessionFactory sqlSessionFactory;
	
	public void init(String[] mappers){
		managedDataSource = new TxManagedDataSource(DataSourceHolder.getInstance().getDataSource());
		ServiceFactory.getService(TransactionManager.class).addTransactionHandler(managedDataSource);

		//创建txfactory，不提交，不关闭，一切交给容器
		Properties prop = new Properties();
		prop.setProperty("closeConnection", "false");
		ManagedTransactionFactory tm = new ManagedTransactionFactory();
		tm.setProperties(prop);

		//create session factory
		Environment environment = new Environment("mybatis", tm, managedDataSource);
		Configuration configuration = new Configuration(environment);
		
		for (String c:mappers){
			try {
				if (c.endsWith(".xml")||c.endsWith(".XML")){
					 InputStream inputStream = Resources.getResourceAsStream(c);
			         XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, c, configuration.getSqlFragments());
			         mapperParser.parse();
				}else{
					//check xml file exists in classpath
					String tryFile = StringKit.replaceStr(c, ".", "/") + ".xml";
					boolean exists = false;
					InputStream stream = null;
					try {
						stream = this.getClass().getClassLoader().getResourceAsStream(tryFile);
						if (stream != null)
							exists = true;
					} finally {
						if (stream != null)try {stream.close();} catch (Exception e) {}
					}
					
					//load the mapper
					if (!exists)
						configuration.addMapper(Class.forName(c));
					else{
						 c = tryFile;
						 InputStream inputStream = Resources.getResourceAsStream(c);
				         XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, c, configuration.getSqlFragments());
				         mapperParser.parse();
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		MybatsiInterceptorManager.instance.installPlugins(configuration);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
	}
	
	/* (non-Javadoc)
	 * @see net.luis.plugin.das.mybatis.impl.IMybatisService#openSession()
	 */
	@Override
	public SqlSession openSession(){
		return sqlSessionFactory.openSession();
	}
	
	/* (non-Javadoc)
	 * @see net.luis.plugin.das.mybatis.impl.IMybatisService#runWithMapper(java.lang.Class, net.luis.plugin.das.mybatis.impl.IMapperHandler)
	 */
	@Override
	public <T> void runWithMapper(Class<T> type,IMapperHandler<T> handler){
		SqlSession sess = openSession();
		try{
			T mapper = sqlSessionFactory.getConfiguration().getMapper(type, sess);
			handler.run(mapper);
		}finally{
			sess.close();
		}
	}

	@Override
	public <M,R> R returnWithMapper(Class<M> type,IMapperHandlerForReturn<M,R> handler){
		SqlSession sess = openSession();
		try{
			M mapper = sqlSessionFactory.getConfiguration().getMapper(type, sess);
			return handler.fetchResult(mapper);
		}finally{
			sess.close();
		}
	}

	
	/**
	 * 注意，这里获取的connection不用关闭，会自动关闭的
	 */
	@Override
	public Connection getConnection() {
		try {
			return managedDataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
