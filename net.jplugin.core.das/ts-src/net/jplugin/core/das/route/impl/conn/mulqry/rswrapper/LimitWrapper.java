package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
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
import java.util.Map;

import net.jplugin.core.das.route.impl.CombinedSqlContext;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.LimitWrapperController.LimitInfo;

public class LimitWrapper implements ResultSet{

	private ResultSet inner;
	private LimitInfo limitInfo;
	private boolean afterLast;
	long count=0;

	public LimitWrapper(ResultSet rs, LimitInfo info) {
		this.inner = rs;
		this.limitInfo = info;
		
		//先滚到制定位置
		for (int i=0;i<limitInfo.getOffset();i++){
			try {
				if (!rs.next()){
					this.afterLast = true;
					break;
				}
			} catch (SQLException e) {
				throw new RuntimeException("Sql feth for limit error."+CombinedSqlContext.get().getOriginalSql());
			}
		}
	}

	public boolean next() throws SQLException {
		if (this.afterLast) 
			return false;

		if (this.count>=this.limitInfo.getRowCount()) 
			return false;
		
		this.afterLast = !inner.next();
		this.count ++;
		
		return !this.afterLast;
	}
	
	
	//下面都是默认实现
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return inner.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return inner.isWrapperFor(iface);
	}



	public void close() throws SQLException {
		inner.close();
	}

	public boolean wasNull() throws SQLException {
		return inner.wasNull();
	}

	public String getString(int columnIndex) throws SQLException {
		return inner.getString(columnIndex);
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		return inner.getBoolean(columnIndex);
	}

	public byte getByte(int columnIndex) throws SQLException {
		return inner.getByte(columnIndex);
	}

	public short getShort(int columnIndex) throws SQLException {
		return inner.getShort(columnIndex);
	}

	public int getInt(int columnIndex) throws SQLException {
		return inner.getInt(columnIndex);
	}

	public long getLong(int columnIndex) throws SQLException {
		return inner.getLong(columnIndex);
	}

	public float getFloat(int columnIndex) throws SQLException {
		return inner.getFloat(columnIndex);
	}

	public double getDouble(int columnIndex) throws SQLException {
		return inner.getDouble(columnIndex);
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return inner.getBigDecimal(columnIndex, scale);
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		return inner.getBytes(columnIndex);
	}

	public Date getDate(int columnIndex) throws SQLException {
		return inner.getDate(columnIndex);
	}

	public Time getTime(int columnIndex) throws SQLException {
		return inner.getTime(columnIndex);
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return inner.getTimestamp(columnIndex);
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return inner.getAsciiStream(columnIndex);
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return inner.getUnicodeStream(columnIndex);
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return inner.getBinaryStream(columnIndex);
	}

	public String getString(String columnLabel) throws SQLException {
		return inner.getString(columnLabel);
	}

	public boolean getBoolean(String columnLabel) throws SQLException {
		return inner.getBoolean(columnLabel);
	}

	public byte getByte(String columnLabel) throws SQLException {
		return inner.getByte(columnLabel);
	}

	public short getShort(String columnLabel) throws SQLException {
		return inner.getShort(columnLabel);
	}

	public int getInt(String columnLabel) throws SQLException {
		return inner.getInt(columnLabel);
	}

	public long getLong(String columnLabel) throws SQLException {
		return inner.getLong(columnLabel);
	}

	public float getFloat(String columnLabel) throws SQLException {
		return inner.getFloat(columnLabel);
	}

	public double getDouble(String columnLabel) throws SQLException {
		return inner.getDouble(columnLabel);
	}

	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return inner.getBigDecimal(columnLabel, scale);
	}

	public byte[] getBytes(String columnLabel) throws SQLException {
		return inner.getBytes(columnLabel);
	}

	public Date getDate(String columnLabel) throws SQLException {
		return inner.getDate(columnLabel);
	}

	public Time getTime(String columnLabel) throws SQLException {
		return inner.getTime(columnLabel);
	}

	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return inner.getTimestamp(columnLabel);
	}

	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return inner.getAsciiStream(columnLabel);
	}

	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return inner.getUnicodeStream(columnLabel);
	}

	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return inner.getBinaryStream(columnLabel);
	}

	public SQLWarning getWarnings() throws SQLException {
		return inner.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		inner.clearWarnings();
	}

	public String getCursorName() throws SQLException {
		return inner.getCursorName();
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return inner.getMetaData();
	}

	public Object getObject(int columnIndex) throws SQLException {
		return inner.getObject(columnIndex);
	}

	public Object getObject(String columnLabel) throws SQLException {
		return inner.getObject(columnLabel);
	}

	public int findColumn(String columnLabel) throws SQLException {
		return inner.findColumn(columnLabel);
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return inner.getCharacterStream(columnIndex);
	}

	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return inner.getCharacterStream(columnLabel);
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return inner.getBigDecimal(columnIndex);
	}

	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return inner.getBigDecimal(columnLabel);
	}

	public boolean isBeforeFirst() throws SQLException {
		return inner.isBeforeFirst();
	}

	public boolean isAfterLast() throws SQLException {
		return inner.isAfterLast();
	}

	public boolean isFirst() throws SQLException {
		return inner.isFirst();
	}

	public boolean isLast() throws SQLException {
		return inner.isLast();
	}

	public void beforeFirst() throws SQLException {
		inner.beforeFirst();
	}

	public void afterLast() throws SQLException {
		inner.afterLast();
	}

	public boolean first() throws SQLException {
		return inner.first();
	}

	public boolean last() throws SQLException {
		return inner.last();
	}

	public int getRow() throws SQLException {
		return inner.getRow();
	}

	public boolean absolute(int row) throws SQLException {
		return inner.absolute(row);
	}

	public boolean relative(int rows) throws SQLException {
		return inner.relative(rows);
	}

	public boolean previous() throws SQLException {
		return inner.previous();
	}

	public void setFetchDirection(int direction) throws SQLException {
		inner.setFetchDirection(direction);
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

	public int getType() throws SQLException {
		return inner.getType();
	}

	public int getConcurrency() throws SQLException {
		return inner.getConcurrency();
	}

	public boolean rowUpdated() throws SQLException {
		return inner.rowUpdated();
	}

	public boolean rowInserted() throws SQLException {
		return inner.rowInserted();
	}

	public boolean rowDeleted() throws SQLException {
		return inner.rowDeleted();
	}

	public void updateNull(int columnIndex) throws SQLException {
		inner.updateNull(columnIndex);
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		inner.updateBoolean(columnIndex, x);
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		inner.updateByte(columnIndex, x);
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		inner.updateShort(columnIndex, x);
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		inner.updateInt(columnIndex, x);
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		inner.updateLong(columnIndex, x);
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		inner.updateFloat(columnIndex, x);
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		inner.updateDouble(columnIndex, x);
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		inner.updateBigDecimal(columnIndex, x);
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		inner.updateString(columnIndex, x);
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		inner.updateBytes(columnIndex, x);
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		inner.updateDate(columnIndex, x);
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		inner.updateTime(columnIndex, x);
	}

	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		inner.updateTimestamp(columnIndex, x);
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		inner.updateAsciiStream(columnIndex, x, length);
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		inner.updateBinaryStream(columnIndex, x, length);
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		inner.updateCharacterStream(columnIndex, x, length);
	}

	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		inner.updateObject(columnIndex, x, scaleOrLength);
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		inner.updateObject(columnIndex, x);
	}

	public void updateNull(String columnLabel) throws SQLException {
		inner.updateNull(columnLabel);
	}

	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		inner.updateBoolean(columnLabel, x);
	}

	public void updateByte(String columnLabel, byte x) throws SQLException {
		inner.updateByte(columnLabel, x);
	}

	public void updateShort(String columnLabel, short x) throws SQLException {
		inner.updateShort(columnLabel, x);
	}

	public void updateInt(String columnLabel, int x) throws SQLException {
		inner.updateInt(columnLabel, x);
	}

	public void updateLong(String columnLabel, long x) throws SQLException {
		inner.updateLong(columnLabel, x);
	}

	public void updateFloat(String columnLabel, float x) throws SQLException {
		inner.updateFloat(columnLabel, x);
	}

	public void updateDouble(String columnLabel, double x) throws SQLException {
		inner.updateDouble(columnLabel, x);
	}

	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		inner.updateBigDecimal(columnLabel, x);
	}

	public void updateString(String columnLabel, String x) throws SQLException {
		inner.updateString(columnLabel, x);
	}

	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		inner.updateBytes(columnLabel, x);
	}

	public void updateDate(String columnLabel, Date x) throws SQLException {
		inner.updateDate(columnLabel, x);
	}

	public void updateTime(String columnLabel, Time x) throws SQLException {
		inner.updateTime(columnLabel, x);
	}

	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		inner.updateTimestamp(columnLabel, x);
	}

	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		inner.updateAsciiStream(columnLabel, x, length);
	}

	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		inner.updateBinaryStream(columnLabel, x, length);
	}

	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		inner.updateCharacterStream(columnLabel, reader, length);
	}

	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		inner.updateObject(columnLabel, x, scaleOrLength);
	}

	public void updateObject(String columnLabel, Object x) throws SQLException {
		inner.updateObject(columnLabel, x);
	}

	public void insertRow() throws SQLException {
		inner.insertRow();
	}

	public void updateRow() throws SQLException {
		inner.updateRow();
	}

	public void deleteRow() throws SQLException {
		inner.deleteRow();
	}

	public void refreshRow() throws SQLException {
		inner.refreshRow();
	}

	public void cancelRowUpdates() throws SQLException {
		inner.cancelRowUpdates();
	}

	public void moveToInsertRow() throws SQLException {
		inner.moveToInsertRow();
	}

	public void moveToCurrentRow() throws SQLException {
		inner.moveToCurrentRow();
	}

	public Statement getStatement() throws SQLException {
		return inner.getStatement();
	}

	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		return inner.getObject(columnIndex, map);
	}

	public Ref getRef(int columnIndex) throws SQLException {
		return inner.getRef(columnIndex);
	}

	public Blob getBlob(int columnIndex) throws SQLException {
		return inner.getBlob(columnIndex);
	}

	public Clob getClob(int columnIndex) throws SQLException {
		return inner.getClob(columnIndex);
	}

	public Array getArray(int columnIndex) throws SQLException {
		return inner.getArray(columnIndex);
	}

	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		return inner.getObject(columnLabel, map);
	}

	public Ref getRef(String columnLabel) throws SQLException {
		return inner.getRef(columnLabel);
	}

	public Blob getBlob(String columnLabel) throws SQLException {
		return inner.getBlob(columnLabel);
	}

	public Clob getClob(String columnLabel) throws SQLException {
		return inner.getClob(columnLabel);
	}

	public Array getArray(String columnLabel) throws SQLException {
		return inner.getArray(columnLabel);
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return inner.getDate(columnIndex, cal);
	}

	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return inner.getDate(columnLabel, cal);
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return inner.getTime(columnIndex, cal);
	}

	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return inner.getTime(columnLabel, cal);
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return inner.getTimestamp(columnIndex, cal);
	}

	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		return inner.getTimestamp(columnLabel, cal);
	}

	public URL getURL(int columnIndex) throws SQLException {
		return inner.getURL(columnIndex);
	}

	public URL getURL(String columnLabel) throws SQLException {
		return inner.getURL(columnLabel);
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		inner.updateRef(columnIndex, x);
	}

	public void updateRef(String columnLabel, Ref x) throws SQLException {
		inner.updateRef(columnLabel, x);
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		inner.updateBlob(columnIndex, x);
	}

	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		inner.updateBlob(columnLabel, x);
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		inner.updateClob(columnIndex, x);
	}

	public void updateClob(String columnLabel, Clob x) throws SQLException {
		inner.updateClob(columnLabel, x);
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		inner.updateArray(columnIndex, x);
	}

	public void updateArray(String columnLabel, Array x) throws SQLException {
		inner.updateArray(columnLabel, x);
	}

	public RowId getRowId(int columnIndex) throws SQLException {
		return inner.getRowId(columnIndex);
	}

	public RowId getRowId(String columnLabel) throws SQLException {
		return inner.getRowId(columnLabel);
	}

	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		inner.updateRowId(columnIndex, x);
	}

	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		inner.updateRowId(columnLabel, x);
	}

	public int getHoldability() throws SQLException {
		return inner.getHoldability();
	}

	public boolean isClosed() throws SQLException {
		return inner.isClosed();
	}

	public void updateNString(int columnIndex, String nString) throws SQLException {
		inner.updateNString(columnIndex, nString);
	}

	public void updateNString(String columnLabel, String nString) throws SQLException {
		inner.updateNString(columnLabel, nString);
	}

	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		inner.updateNClob(columnIndex, nClob);
	}

	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		inner.updateNClob(columnLabel, nClob);
	}

	public NClob getNClob(int columnIndex) throws SQLException {
		return inner.getNClob(columnIndex);
	}

	public NClob getNClob(String columnLabel) throws SQLException {
		return inner.getNClob(columnLabel);
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return inner.getSQLXML(columnIndex);
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return inner.getSQLXML(columnLabel);
	}

	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		inner.updateSQLXML(columnIndex, xmlObject);
	}

	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		inner.updateSQLXML(columnLabel, xmlObject);
	}

	public String getNString(int columnIndex) throws SQLException {
		return inner.getNString(columnIndex);
	}

	public String getNString(String columnLabel) throws SQLException {
		return inner.getNString(columnLabel);
	}

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return inner.getNCharacterStream(columnIndex);
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return inner.getNCharacterStream(columnLabel);
	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		inner.updateNCharacterStream(columnIndex, x, length);
	}

	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		inner.updateNCharacterStream(columnLabel, reader, length);
	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		inner.updateAsciiStream(columnIndex, x, length);
	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		inner.updateBinaryStream(columnIndex, x, length);
	}

	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		inner.updateCharacterStream(columnIndex, x, length);
	}

	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		inner.updateAsciiStream(columnLabel, x, length);
	}

	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		inner.updateBinaryStream(columnLabel, x, length);
	}

	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		inner.updateCharacterStream(columnLabel, reader, length);
	}

	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		inner.updateBlob(columnIndex, inputStream, length);
	}

	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		inner.updateBlob(columnLabel, inputStream, length);
	}

	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		inner.updateClob(columnIndex, reader, length);
	}

	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		inner.updateClob(columnLabel, reader, length);
	}

	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		inner.updateNClob(columnIndex, reader, length);
	}

	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		inner.updateNClob(columnLabel, reader, length);
	}

	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		inner.updateNCharacterStream(columnIndex, x);
	}

	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		inner.updateNCharacterStream(columnLabel, reader);
	}

	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		inner.updateAsciiStream(columnIndex, x);
	}

	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		inner.updateBinaryStream(columnIndex, x);
	}

	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		inner.updateCharacterStream(columnIndex, x);
	}

	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		inner.updateAsciiStream(columnLabel, x);
	}

	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		inner.updateBinaryStream(columnLabel, x);
	}

	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		inner.updateCharacterStream(columnLabel, reader);
	}

	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		inner.updateBlob(columnIndex, inputStream);
	}

	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		inner.updateBlob(columnLabel, inputStream);
	}

	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		inner.updateClob(columnIndex, reader);
	}

	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		inner.updateClob(columnLabel, reader);
	}

	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		inner.updateNClob(columnIndex, reader);
	}

	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		inner.updateNClob(columnLabel, reader);
	}

	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return inner.getObject(columnIndex, type);
	}

	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		return inner.getObject(columnLabel, type);
	}

	public  void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength)
			throws SQLException {
		inner.updateObject(columnIndex, x, targetSqlType, scaleOrLength);
	}

	public  void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength)
			throws SQLException {
		inner.updateObject(columnLabel, x, targetSqlType, scaleOrLength);
	}

	public  void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
		inner.updateObject(columnIndex, x, targetSqlType);
	}

	public  void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
		inner.updateObject(columnLabel, x, targetSqlType);
	}

	
}
