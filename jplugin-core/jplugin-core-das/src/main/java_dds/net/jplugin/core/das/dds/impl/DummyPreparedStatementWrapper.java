package net.jplugin.core.das.dds.impl;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 这个类目前没有使用，不包装也没问题
 * @author Administrator
 *
 */
public class DummyPreparedStatementWrapper implements PreparedStatement{
	
	PreparedStatement inner;
	private DummyConnection connection;
	public DummyPreparedStatementWrapper(DummyConnection conn,PreparedStatement o) {
		this.connection = conn;
		this.inner = o;
	}

	public Connection getConnection() throws SQLException {
		return connection;
	}
	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return inner.unwrap(iface);
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return inner.executeQuery(sql);
	}

	public ResultSet executeQuery() throws SQLException {
		return inner.executeQuery();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return inner.isWrapperFor(iface);
	}

	public int executeUpdate(String sql) throws SQLException {
		return inner.executeUpdate(sql);
	}

	public int executeUpdate() throws SQLException {
		return inner.executeUpdate();
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		inner.setNull(parameterIndex, sqlType);
	}

	public void close() throws SQLException {
		inner.close();
	}

	public int getMaxFieldSize() throws SQLException {
		return inner.getMaxFieldSize();
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		inner.setBoolean(parameterIndex, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		inner.setByte(parameterIndex, x);
	}

	public void setMaxFieldSize(int max) throws SQLException {
		inner.setMaxFieldSize(max);
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		inner.setShort(parameterIndex, x);
	}

	public int getMaxRows() throws SQLException {
		return inner.getMaxRows();
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		inner.setInt(parameterIndex, x);
	}

	public void setMaxRows(int max) throws SQLException {
		inner.setMaxRows(max);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		inner.setLong(parameterIndex, x);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		inner.setFloat(parameterIndex, x);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		inner.setEscapeProcessing(enable);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		inner.setDouble(parameterIndex, x);
	}

	public int getQueryTimeout() throws SQLException {
		return inner.getQueryTimeout();
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		inner.setBigDecimal(parameterIndex, x);
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		inner.setQueryTimeout(seconds);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		inner.setString(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		inner.setBytes(parameterIndex, x);
	}

	public void cancel() throws SQLException {
		inner.cancel();
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		inner.setDate(parameterIndex, x);
	}

	public SQLWarning getWarnings() throws SQLException {
		return inner.getWarnings();
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		inner.setTime(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		inner.setTimestamp(parameterIndex, x);
	}

	public void clearWarnings() throws SQLException {
		inner.clearWarnings();
	}

	public void setCursorName(String name) throws SQLException {
		inner.setCursorName(name);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		inner.setAsciiStream(parameterIndex, x, length);
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		inner.setUnicodeStream(parameterIndex, x, length);
	}

	public boolean execute(String sql) throws SQLException {
		return inner.execute(sql);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		inner.setBinaryStream(parameterIndex, x, length);
	}

	public ResultSet getResultSet() throws SQLException {
		return inner.getResultSet();
	}

	public int getUpdateCount() throws SQLException {
		return inner.getUpdateCount();
	}

	public void clearParameters() throws SQLException {
		inner.clearParameters();
	}

	public boolean getMoreResults() throws SQLException {
		return inner.getMoreResults();
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		inner.setObject(parameterIndex, x, targetSqlType);
	}

	public void setFetchDirection(int direction) throws SQLException {
		inner.setFetchDirection(direction);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		inner.setObject(parameterIndex, x);
	}

	public int getFetchDirection() throws SQLException {
		return inner.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		inner.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return inner.getFetchSize();
	}

	public boolean execute() throws SQLException {
		return inner.execute();
	}

	public int getResultSetConcurrency() throws SQLException {
		return inner.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return inner.getResultSetType();
	}

	public void addBatch(String sql) throws SQLException {
		inner.addBatch(sql);
	}

	public void addBatch() throws SQLException {
		inner.addBatch();
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		inner.setCharacterStream(parameterIndex, reader, length);
	}

	public void clearBatch() throws SQLException {
		inner.clearBatch();
	}

	public int[] executeBatch() throws SQLException {
		return inner.executeBatch();
	}

	public void setRef(int parameterIndex, Ref x) throws SQLException {
		inner.setRef(parameterIndex, x);
	}

	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		inner.setBlob(parameterIndex, x);
	}

	public void setClob(int parameterIndex, Clob x) throws SQLException {
		inner.setClob(parameterIndex, x);
	}

	public void setArray(int parameterIndex, Array x) throws SQLException {
		inner.setArray(parameterIndex, x);
	}



	public ResultSetMetaData getMetaData() throws SQLException {
		return inner.getMetaData();
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		inner.setDate(parameterIndex, x, cal);
	}

	public boolean getMoreResults(int current) throws SQLException {
		return inner.getMoreResults(current);
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		inner.setTime(parameterIndex, x, cal);
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return inner.getGeneratedKeys();
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		inner.setTimestamp(parameterIndex, x, cal);
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		return inner.executeUpdate(sql, autoGeneratedKeys);
	}

	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		inner.setNull(parameterIndex, sqlType, typeName);
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		return inner.executeUpdate(sql, columnIndexes);
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		inner.setURL(parameterIndex, x);
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return inner.getParameterMetaData();
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		inner.setRowId(parameterIndex, x);
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		return inner.executeUpdate(sql, columnNames);
	}

	public void setNString(int parameterIndex, String value) throws SQLException {
		inner.setNString(parameterIndex, value);
	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		inner.setNCharacterStream(parameterIndex, value, length);
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		return inner.execute(sql, autoGeneratedKeys);
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		inner.setNClob(parameterIndex, value);
	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		inner.setClob(parameterIndex, reader, length);
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return inner.execute(sql, columnIndexes);
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		inner.setBlob(parameterIndex, inputStream, length);
	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		inner.setNClob(parameterIndex, reader, length);
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		return inner.execute(sql, columnNames);
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		inner.setSQLXML(parameterIndex, xmlObject);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		inner.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	public int getResultSetHoldability() throws SQLException {
		return inner.getResultSetHoldability();
	}

	public boolean isClosed() throws SQLException {
		return inner.isClosed();
	}

	public void setPoolable(boolean poolable) throws SQLException {
		inner.setPoolable(poolable);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		inner.setAsciiStream(parameterIndex, x, length);
	}

	public boolean isPoolable() throws SQLException {
		return inner.isPoolable();
	}

	public void closeOnCompletion() throws SQLException {
		inner.closeOnCompletion();
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		inner.setBinaryStream(parameterIndex, x, length);
	}

	public boolean isCloseOnCompletion() throws SQLException {
		return inner.isCloseOnCompletion();
	}

	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		inner.setCharacterStream(parameterIndex, reader, length);
	}

	public  long getLargeUpdateCount() throws SQLException {
		return inner.getLargeUpdateCount();
	}

	public  void setLargeMaxRows(long max) throws SQLException {
		inner.setLargeMaxRows(max);
	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		inner.setAsciiStream(parameterIndex, x);
	}

	public  long getLargeMaxRows() throws SQLException {
		return inner.getLargeMaxRows();
	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		inner.setBinaryStream(parameterIndex, x);
	}

	public  long[] executeLargeBatch() throws SQLException {
		return inner.executeLargeBatch();
	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		inner.setCharacterStream(parameterIndex, reader);
	}

	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		inner.setNCharacterStream(parameterIndex, value);
	}

	public  long executeLargeUpdate(String sql) throws SQLException {
		return inner.executeLargeUpdate(sql);
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		inner.setClob(parameterIndex, reader);
	}

	public  long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		return inner.executeLargeUpdate(sql, autoGeneratedKeys);
	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		inner.setBlob(parameterIndex, inputStream);
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		inner.setNClob(parameterIndex, reader);
	}

	public  long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
		return inner.executeLargeUpdate(sql, columnIndexes);
	}

	public  void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength)
			throws SQLException {
		inner.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	public  long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
		return inner.executeLargeUpdate(sql, columnNames);
	}

	public  void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
		inner.setObject(parameterIndex, x, targetSqlType);
	}

	public  long executeLargeUpdate() throws SQLException {
		return inner.executeLargeUpdate();
	}
	

}
