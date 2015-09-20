package net.jplugin.core.das.hib.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.connection.ConnectionProvider;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-18 上午09:58:02
 **/

public class DBConnectionProvider implements ConnectionProvider {
	DataSource inner = null;
	public static ThreadLocal<DataSource> ds = new ThreadLocal<DataSource>();

	public DBConnectionProvider(){
		this.inner = ds.get();
		if (this.inner== null){
			throw new RuntimeException("the ds in thread local is null");
		}
	}
	
	public void closeConnection(Connection c) throws SQLException {
		c.close();
	}

	public void configure(Properties arg0) throws HibernateException {
	}

	public Connection getConnection() throws SQLException {
		return inner.getConnection();
	}

	public boolean supportsAggressiveRelease() {
		return false;
	}


	public void close() throws HibernateException {
	}

}
