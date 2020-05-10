package net.jplugin.core.das.mybatis.impl;

import java.sql.Connection;

import org.apache.ibatis.session.SqlSession;

import net.jplugin.core.das.mybatis.api.IMapperHandlerForReturn;

public interface IMybatisService {

	public <T> T getMapper(Class<T> t);
	
	public abstract SqlSession openSession();
	
	/**
	 * 以后可以直接简单的从@RefMapper方式获取Mapper, 或者从MybasticService获取Session，获取Mapper即可，无需关闭了。
	 * @param type
	 * @param handler
	 */
	@Deprecated 
	public abstract <T> void runWithMapper(Class<T> type, IMapperHandler<T> handler);

	/**
	 * 以后可以直接简单的从@RefMapper方式获取Mapper, 或者从MybasticService获取Session，获取Mapper即可，无需关闭了。
	 * @param type
	 * @param handler
	 */
	@Deprecated
	public <M,R> R returnWithMapper(Class<M> type,IMapperHandlerForReturn<M,R> handler);
	
	public Connection getConnection();
	
	public boolean containsMapper(String clazz);

}