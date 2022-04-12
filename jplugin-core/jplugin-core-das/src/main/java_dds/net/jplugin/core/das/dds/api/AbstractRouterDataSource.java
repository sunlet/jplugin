package net.jplugin.core.das.dds.api;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import net.jplugin.core.das.dds.impl.DummyConnection;

public abstract class AbstractRouterDataSource implements IRouterDataSource, DataSource {
	private PrintWriter logger;

	@Override
	public Connection getConnection() throws SQLException {
		Connection conn = this.getTargetConnBefConnect();
		if (conn!=null) {
			return conn;
		}else {
			return DummyConnection.create(this);
		}
	}
	

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		throw new RuntimeException("not support");
	}


	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return logger;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.logger = out;
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (iface.equals(this.getClass()))
			return (T) this;
		else
			throw new RuntimeException("can't unwarp");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new RuntimeException("not impl");
	}

}
