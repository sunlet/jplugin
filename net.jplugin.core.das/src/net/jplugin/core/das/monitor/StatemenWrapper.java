package net.jplugin.core.das.monitor;

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
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import net.jplugin.core.das.api.monitor.SqlMonitorListenerContext;
import net.jplugin.core.das.api.monitor.StatementContext;

public class StatemenWrapper implements PreparedStatement {
	Statement inner;
	String theSql;
	private String dataSourceName;
	private StatementContext ctx;

	public StatemenWrapper(String dsName, Statement s, String sql) {
		this.inner = s;
		this.theSql = sql;
		this.dataSourceName = dsName;
	}
	
	public StatementContext getCtx() {
		//目前在SqlMonitor.execute 的时候调用context，sql的值已经有了
		if (this.ctx == null) {
			this.ctx = SqlMonitorListenerContext.createStatementCtx();
			ctx.setSql(theSql);
			ctx.setDataSource(dataSourceName);
		}
		return ctx;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	// FOR PREPARED STATEMENT
	public ResultSet executeQuery() throws SQLException {
		return (ResultSet) SqlMonitor.execute(this, "executeQuery", theSql,
				() -> ((PreparedStatement) inner).executeQuery());
	}

	public int executeUpdate() throws SQLException {
		return (int) SqlMonitor.execute(this, "executeUpdate", theSql,
				() -> ((PreparedStatement) inner).executeUpdate());
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		((PreparedStatement) inner).setNull(parameterIndex, sqlType);
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		((PreparedStatement) inner).setBoolean(parameterIndex, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		((PreparedStatement) inner).setByte(parameterIndex, x);
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		((PreparedStatement) inner).setShort(parameterIndex, x);
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		((PreparedStatement) inner).setInt(parameterIndex, x);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		((PreparedStatement) inner).setLong(parameterIndex, x);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		((PreparedStatement) inner).setFloat(parameterIndex, x);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		((PreparedStatement) inner).setDouble(parameterIndex, x);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		((PreparedStatement) inner).setBigDecimal(parameterIndex, x);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		((PreparedStatement) inner).setString(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		((PreparedStatement) inner).setBytes(parameterIndex, x);
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		((PreparedStatement) inner).setDate(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		((PreparedStatement) inner).setTime(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		((PreparedStatement) inner).setTimestamp(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		((PreparedStatement) inner).setAsciiStream(parameterIndex, x, length);
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		((PreparedStatement) inner).setUnicodeStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		((PreparedStatement) inner).setBinaryStream(parameterIndex, x, length);
	}

	public void clearParameters() throws SQLException {
		((PreparedStatement) inner).clearParameters();
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		((PreparedStatement) inner).setObject(parameterIndex, x, targetSqlType);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		((PreparedStatement) inner).setObject(parameterIndex, x);
	}

	public boolean execute() throws SQLException {
		return (boolean) SqlMonitor.execute(this, "execute", theSql, () -> ((PreparedStatement) inner).execute());
	}

	public void addBatch() throws SQLException {
		((PreparedStatement) inner).addBatch();
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		((PreparedStatement) inner).setCharacterStream(parameterIndex, reader, length);
	}

	public void setRef(int parameterIndex, Ref x) throws SQLException {
		((PreparedStatement) inner).setRef(parameterIndex, x);
	}

	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		((PreparedStatement) inner).setBlob(parameterIndex, x);
	}

	public void setClob(int parameterIndex, Clob x) throws SQLException {
		((PreparedStatement) inner).setClob(parameterIndex, x);
	}

	public void setArray(int parameterIndex, Array x) throws SQLException {
		((PreparedStatement) inner).setArray(parameterIndex, x);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return ((PreparedStatement) inner).getMetaData();
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		((PreparedStatement) inner).setDate(parameterIndex, x, cal);
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		((PreparedStatement) inner).setTime(parameterIndex, x, cal);
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		((PreparedStatement) inner).setTimestamp(parameterIndex, x, cal);
	}

	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		((PreparedStatement) inner).setNull(parameterIndex, sqlType, typeName);
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		((PreparedStatement) inner).setURL(parameterIndex, x);
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return ((PreparedStatement) inner).getParameterMetaData();
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		((PreparedStatement) inner).setRowId(parameterIndex, x);
	}

	public void setNString(int parameterIndex, String value) throws SQLException {
		((PreparedStatement) inner).setNString(parameterIndex, value);
	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		((PreparedStatement) inner).setNCharacterStream(parameterIndex, value, length);
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		((PreparedStatement) inner).setNClob(parameterIndex, value);
	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		((PreparedStatement) inner).setClob(parameterIndex, reader, length);
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		((PreparedStatement) inner).setBlob(parameterIndex, inputStream, length);
	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		((PreparedStatement) inner).setNClob(parameterIndex, reader, length);
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		((PreparedStatement) inner).setSQLXML(parameterIndex, xmlObject);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		((PreparedStatement) inner).setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		((PreparedStatement) inner).setAsciiStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		((PreparedStatement) inner).setBinaryStream(parameterIndex, x, length);
	}

	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		((PreparedStatement) inner).setCharacterStream(parameterIndex, reader, length);
	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		((PreparedStatement) inner).setAsciiStream(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		((PreparedStatement) inner).setBinaryStream(parameterIndex, x);
	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		((PreparedStatement) inner).setCharacterStream(parameterIndex, reader);
	}

	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		((PreparedStatement) inner).setNCharacterStream(parameterIndex, value);
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		((PreparedStatement) inner).setClob(parameterIndex, reader);
	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		((PreparedStatement) inner).setBlob(parameterIndex, inputStream);
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		((PreparedStatement) inner).setNClob(parameterIndex, reader);
	}

	public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
		((PreparedStatement) inner).setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
		((PreparedStatement) inner).setObject(parameterIndex, x, targetSqlType);
	}

	public long executeLargeUpdate() throws SQLException {
		return (long) SqlMonitor.execute(this, "executeLargeUpdate", theSql,
				() -> ((PreparedStatement) inner).executeLargeUpdate());
	}

	// FOR STATEMENT
	public void close() throws SQLException {
		inner.close();
	}

	public void cancel() throws SQLException {
		inner.cancel();
	}

	public void clearWarnings() throws SQLException {
		inner.clearWarnings();
	}

	public boolean execute(String sql) throws SQLException {
		this.theSql = sql;
		return (boolean) SqlMonitor.execute(this, "execute", theSql, () -> inner.execute(sql));
	}

	public void addBatch(String sql) throws SQLException {
		inner.addBatch(sql);
	}

	public void clearBatch() throws SQLException {
		inner.clearBatch();
	}

	public int[] executeBatch() throws SQLException {
		return (int[]) SqlMonitor.execute(this, "executeBatch", theSql, () -> inner.executeBatch());
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		this.theSql = sql;
		return (boolean) SqlMonitor.execute(this, "execute", theSql, () -> inner.execute(sql, columnNames));
	}

	public void closeOnCompletion() throws SQLException {
		inner.closeOnCompletion();
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		this.theSql = sql;
		return (ResultSet) SqlMonitor.execute(this, "executeQuery", theSql, () -> inner.executeQuery(sql));
	}

	public int executeUpdate(String sql) throws SQLException {
		this.theSql = sql;
		return (int) SqlMonitor.execute(this, "executeUpdate", theSql, () -> inner.executeUpdate(sql));
	}

	public int getFetchDirection() throws SQLException {
		return inner.getFetchDirection();
	}

	public int getFetchSize() throws SQLException {
		return inner.getFetchSize();
	}

	public Connection getConnection() throws SQLException {
		return inner.getConnection();
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return inner.getGeneratedKeys();
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		this.theSql = sql;
		return (int) SqlMonitor.execute(this, "executeUpdate", theSql,
				() -> inner.executeUpdate(sql, autoGeneratedKeys));
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		this.theSql = sql;
		return (int) SqlMonitor.execute(this, "executeUpdate", theSql, () -> inner.executeUpdate(sql, columnIndexes));
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		this.theSql = sql;
		return (int) SqlMonitor.execute(this, "executeUpdate", theSql, () -> inner.executeUpdate(sql, columnNames));
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		this.theSql = sql;
		return (boolean) SqlMonitor.execute(this, "execute", theSql, () -> inner.execute(sql, autoGeneratedKeys));
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		this.theSql = sql;
		return (boolean) SqlMonitor.execute(this, "execute", theSql, () -> inner.execute(sql, columnIndexes));
	}

	public long getLargeUpdateCount() throws SQLException {
		return inner.getLargeUpdateCount();
	}

	public long getLargeMaxRows() throws SQLException {
		return inner.getLargeMaxRows();
	}

	public long[] executeLargeBatch() throws SQLException {
		return (long[]) SqlMonitor.execute(this, "executeLargeBatch", theSql, () -> inner.executeLargeBatch());
	}

	public long executeLargeUpdate(String sql) throws SQLException {
		this.theSql = sql;
		return (long) SqlMonitor.execute(this, "executeLargeUpdate", theSql, () -> inner.executeLargeUpdate(sql));
	}

	public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		this.theSql = sql;
		return (long) SqlMonitor.execute(this, "executeLargeUpdate", theSql,
				() -> inner.executeLargeUpdate(sql, autoGeneratedKeys));
	}

	public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
		this.theSql = sql;
		return (long) SqlMonitor.execute(this, "executeLargeUpdate", theSql,
				() -> inner.executeLargeUpdate(sql, columnIndexes));
	}

	public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
		this.theSql = sql;
		return (long) SqlMonitor.execute(this, "executeLargeUpdate", theSql,
				() -> inner.executeLargeUpdate(sql, columnNames));
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return inner.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return inner.isWrapperFor(iface);
	}

	public int getMaxFieldSize() throws SQLException {
		return inner.getMaxFieldSize();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		inner.setMaxFieldSize(max);
	}

	public int getMaxRows() throws SQLException {
		return inner.getMaxRows();
	}

	public void setMaxRows(int max) throws SQLException {
		inner.setMaxRows(max);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		inner.setEscapeProcessing(enable);
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		inner.setQueryTimeout(seconds);
	}

	public void setCursorName(String name) throws SQLException {
		inner.setCursorName(name);
	}

	public boolean getMoreResults() throws SQLException {
		return inner.getMoreResults();
	}

	public boolean getMoreResults(int current) throws SQLException {
		return inner.getMoreResults(current);
	}

	public int getQueryTimeout() throws SQLException {
		return inner.getQueryTimeout();
	}

	public SQLWarning getWarnings() throws SQLException {
		return inner.getWarnings();
	}

	public ResultSet getResultSet() throws SQLException {
		return inner.getResultSet();
	}

	public int getUpdateCount() throws SQLException {
		return inner.getUpdateCount();
	}

	public void setFetchDirection(int direction) throws SQLException {
		inner.setFetchDirection(direction);
	}

	public void setFetchSize(int rows) throws SQLException {
		inner.setFetchSize(rows);
	}

	public int getResultSetConcurrency() throws SQLException {
		return inner.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return inner.getResultSetType();
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

	public boolean isPoolable() throws SQLException {
		return inner.isPoolable();
	}

	public boolean isCloseOnCompletion() throws SQLException {
		return inner.isCloseOnCompletion();
	}

	public void setLargeMaxRows(long max) throws SQLException {
		inner.setLargeMaxRows(max);
	}



}
