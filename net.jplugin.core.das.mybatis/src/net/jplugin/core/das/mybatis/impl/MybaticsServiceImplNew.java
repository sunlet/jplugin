package net.jplugin.core.das.mybatis.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.mybatis.api.ExtensionMybatisDasHelper;
import net.jplugin.core.das.mybatis.api.IMapperHandlerForReturn;
import net.jplugin.core.das.mybatis.impl.sess.MybatisSessionManager;

public class MybaticsServiceImplNew implements IMybatisService {
	DataSource dataSource=null;
	SqlSessionFactory sqlSessionFactory;
	String theDataSourceName;
	
	public void init(String dataSourceName,List<String> mappers,List<Class> interceptors){
		theDataSourceName = dataSourceName;
		
		if (mappers==null || mappers.size()==0) {
			System.out.println("  No mappers configed.");
			return;
		}
		
//		managedDataSource = new TxManagedDataSource(DataSourceHolder.getInstance().getDataSource());
		
		dataSource = DataSourceFactory.getDataSource(dataSourceName);
//		ServiceFactory.getService(TransactionManager.class).addTransactionHandler(managedDataSource);

		//创建txfactory，不提交，不关闭，一切交给容器
		Properties prop = new Properties();
		prop.setProperty("closeConnection", "false");
		ManagedTransactionFactory tm = new ManagedTransactionFactory();
		tm.setProperties(prop);
		
		//create session factory
		Environment environment = new Environment("mybatis", tm, dataSource);
		Configuration configuration;
		
		//check global config
		String globalConfigResource = checkGlocalConfigMapper(dataSourceName,mappers);
		if (globalConfigResource!=null){
			//注意：在global config情况下，通过扩展配置配置的Inteceptor还是会加入的
			configuration = buildGlobalConfiguration(globalConfigResource);
			configuration.setEnvironment(environment);
		}else{
			configuration = new Configuration(environment);
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
		}
//		MybatsiInterceptorManager.instance.installPlugins(configuration);
		if (interceptors!=null){
			for (Class clazz:interceptors){
				Interceptor incept;
				try {
					incept = (Interceptor) clazz.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("create the mybatis interceptor error: "+clazz.getName());
				}
				configuration.addInterceptor(incept);
			}
		}
		
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
	}
	
	private Configuration buildGlobalConfiguration(String globalConfigResource) {
		InputStream inputStream = null;
		try{
			inputStream = Resources.getResourceAsStream(globalConfigResource);
			if (inputStream==null)
				throw new RuntimeException("Can't find resource for:"+globalConfigResource);
			else
				return new XMLConfigBuilder(inputStream).parse();
		}catch(Exception e){
			throw new RuntimeException("Rource error:"+globalConfigResource,e);
		}finally{
			if (inputStream!=null){
				try{
					inputStream.close();
				}catch(Exception e){}
			}
		}
	}

	/**
	 * 有两个作用：
	 * 1.进行全局配置检查，如果有全局配置，则不允许存在其他配置
	 * 2.返回：是否启用mybatis全局配置
	 * @param datasource
	 * @param mappers
	 * @return
	 */
	private String checkGlocalConfigMapper(String datasource,List<String> mappers) {
		if (mappers==null) return null;
		for (String m:mappers){
			if (m.startsWith(ExtensionMybatisDasHelper.CONFIG_RES_PREFIX)){
				if (mappers.size()!=1){
					throw new RuntimeException("Mybatis config error: The global config ["+m+"] for datasource ["+datasource+"] MUST NOT exists with other mappers. total num:"+mappers.size());
				}
				return m.substring(ExtensionMybatisDasHelper.CONFIG_RES_PREFIX.length());
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.das.mybatis.impl.IMybatisService#openSession()
	 */
	@Override
	public SqlSession openSession(){
//		return sqlSessionFactory.openSession();
		return MybatisSessionManager.getSession(this.theDataSourceName);
	}
	
	/* (non-Javadoc)
	 * @see net.luis.plugin.das.mybatis.impl.IMybatisService#runWithMapper(java.lang.Class, net.luis.plugin.das.mybatis.impl.IMapperHandler)
	 */
	@Override
	public <T> void runWithMapper(Class<T> type,IMapperHandler<T> handler){
		/**
		 * 记录一下：这里的sess.close()目前是空操作，完全可以删去！
		 */
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
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T getMapper(Class<T> t) {
		return openSession().getMapper(t);
	}

	public SqlSession _openRealSession() {
		return sqlSessionFactory.openSession();
	}
}
