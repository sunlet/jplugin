package net.jplugin.core.das.dds.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

public abstract class EmptyStatement implements Statement{

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new RuntimeException("not support");
	}


	@Override
	public int getMaxFieldSize() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public int getMaxRows() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void cancel() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public int getFetchDirection() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public int getFetchSize() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public int getResultSetType() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void clearBatch() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public int[] executeBatch() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		throw new RuntimeException("not support");
	}

//	@Override
//	public ResultSet getGeneratedKeys() throws SQLException {
//		throw new RuntimeException("not support");
//	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		throw new RuntimeException("not support");
	}


	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public boolean isPoolable() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		throw new RuntimeException("not support");
	}

}
