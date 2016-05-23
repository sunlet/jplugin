package net.jplugin.core.das.mybatis.impl;

import java.sql.Connection;

import org.apache.ibatis.session.SqlSession;

import net.jplugin.core.das.mybatis.api.IMapperHandlerForReturn;

public interface IMybatisService {

	public <T> T getMapper(Class<T> t);
	
	public abstract SqlSession openSession();

	public abstract <T> void runWithMapper(Class<T> type, IMapperHandler<T> handler);
	
	public <M,R> R returnWithMapper(Class<M> type,IMapperHandlerForReturn<M,R> handler);
	
	public Connection getConnection();

}