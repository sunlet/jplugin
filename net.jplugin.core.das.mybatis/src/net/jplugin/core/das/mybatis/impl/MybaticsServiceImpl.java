package net.jplugin.core.das.mybatis.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.api.DataSourceHolder;
import net.jplugin.core.das.mybatis.api.IMapperHandlerForReturn;
import net.jplugin.core.service.api.ServiceFactory;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

public class MybaticsServiceImpl implements IMybatisService {
	TxManagedDataSource managedDataSource=null;
	SqlSessionFactory sqlSessionFactory;
	
	public void init(String[] mappers){
		managedDataSource = new TxManagedDataSource(DataSourceHolder.getInstance().getDataSource());
		ServiceFactory.getService(TransactionManager.class).addTransactionHandler(managedDataSource);

		//����txfactory�����ύ�����رգ�һ�н�������
		Properties prop = new Properties();
		prop.setProperty("closeConnection", "false");
		ManagedTransactionFactory tm = new ManagedTransactionFactory();
		tm.setProperties(prop);

		//create session factory
		Environment environment = new Environment("mybatis", tm, managedDataSource);
		Configuration configuration = new Configuration(environment);
		
		for (String c:mappers){
			try {
				configuration.addMapper(Class.forName(c));
			} catch (ClassNotFoundException e) {
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
	 * ע�⣬�����ȡ��connection���ùرգ����Զ��رյ�
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
