package net.jplugin.core.das.mybatis.impl;

import java.sql.Connection;

import org.apache.ibatis.session.SqlSession;

public interface IMybatisService {

	public abstract SqlSession openSession();

	public abstract <T> void runWithMapper(Class<T> type, IMapperHandler<T> handler);
	
	public Connection getConnection();

}