package net.jplugin.core.das.api.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class DataSourceWrapper implements DataSource{
	DataSource inner;
	String dataSourceName;
	public DataSourceWrapper(String dsName, DataSource i){
		this.inner = i;
		this.dataSourceName = dsName;
	}
	
	public Connection getConnection() throws SQLException {
		return ConnectionWrapperManager.getConnection(this.dataSourceName,inner.getConnection());
	}
	public Connection getConnection(String username, String password) throws SQLException {
		return  ConnectionWrapperManager.getConnection(this.dataSourceName,inner.getConnection(username, password));
	}
	
	//delegate methods
	public PrintWriter getLogWriter() throws SQLException {
		return inner.getLogWriter();
	}
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return inner.unwrap(iface);
	}
	public void setLogWriter(PrintWriter out) throws SQLException {
		inner.setLogWriter(out);
	}
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return inner.isWrapperFor(iface);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		inner.setLoginTimeout(seconds);
	}

	public int getLoginTimeout() throws SQLException {
		return inner.getLoginTimeout();
	}
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return inner.getParentLogger();
	}
	
	
}
