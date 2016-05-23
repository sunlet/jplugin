package net.jplugin.core.das.mybatis.impl;

import java.sql.Connection;

import org.apache.ibatis.session.SqlSession;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.mybatis.api.IMapperHandlerForReturn;
import net.jplugin.core.das.mybatis.api.MyBatisServiceFactory;

public class DefaultMybaticsService4JianRong implements IMybatisService{
	
	IMybatisService realSvc =null;
	
	boolean inited=false;
	public void init(){
		if (!inited){
			synchronized (this) {
				realSvc = MyBatisServiceFactory.getService(DataSourceFactory.DATABASE_DSKEY);
				inited = true;
			}
		}
	}
	
	public SqlSession openSession() {
		init();
		return realSvc.openSession();
	}

	public <T> void runWithMapper(Class<T> type, IMapperHandler<T> handler) {
		init();
		realSvc.runWithMapper(type, handler);
	}

	public <M, R> R returnWithMapper(Class<M> type, IMapperHandlerForReturn<M, R> handler) {
		init();
		return realSvc.returnWithMapper(type, handler);
	}

	public Connection getConnection() {
		init();
		return realSvc.getConnection();
	}

	@Override
	public <T> T getMapper(Class<T> t) {
		init();
		return realSvc.getMapper(t);
	}

}
