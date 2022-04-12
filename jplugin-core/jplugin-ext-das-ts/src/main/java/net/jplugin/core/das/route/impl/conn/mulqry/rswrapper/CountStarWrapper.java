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
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import net.jplugin.core.das.dds.kits.EmptyQueryableResultSet;

public class CountStarWrapper extends EmptyQueryableResultSet{
	ResultSet list;
	
	public CountStarWrapper(ResultSet l){
		this.list = l;
	}
	
	long count = -1;

	public boolean next() throws SQLException {
		if (count == -1){
			long temp = 0;
			while(list.next()){
				temp += list.getLong(1);
			}
			count = temp;
			return true;
		}else
			return false;
	}
	
	public byte getByte(int columnIndex) throws SQLException {
		return (byte) count;
	}

	public short getShort(int columnIndex) throws SQLException {
		return (short) count;
	}

	public int getInt(int columnIndex) throws SQLException {
		return (int) count;
	}

	public long getLong(int columnIndex) throws SQLException {
		return count;
	}

	public float getFloat(int columnIndex) throws SQLException {
		return count;
	}

	public double getDouble(int columnIndex) throws SQLException {
		return count;
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return new BigDecimal(count);
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return new BigDecimal(count);
	}
	
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return new BigDecimal(count);
	}
	public Object getObject(int columnIndex) throws SQLException {
		return count;
	}

	public Object getObject(String columnLabel) throws SQLException {
		return count;
	}


	public byte getByte(String columnLabel) throws SQLException {
		return (byte) count;
	}

	public short getShort(String columnLabel) throws SQLException {
		return (short) count;
	}

	public int getInt(String columnLabel) throws SQLException {
		return (int) count;
	}

	public long getLong(String columnLabel) throws SQLException {
		return count;
	}

	public float getFloat(String columnLabel) throws SQLException {
		return count;
	}

	public double getDouble(String columnLabel) throws SQLException {
		return count;
	}

	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return new BigDecimal(count);
	}
	
	public String getString(int columnIndex) throws SQLException {
		return Long.toString(count);
	}
	public String getString(String columnLabel) throws SQLException {
		return Long.toString(count);
	}
	
	/**
	 * 一下为delegate实现
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return list.getMetaData();
	}
	public void close() throws SQLException {
		list.close();
	}

	//对于正常访问，wasNull返回false。 如果调用内层的wasNull,会抛出空指针！ 2020-07-02修复
	public boolean wasNull() throws SQLException {
		return false;
//		return list.wasNull();
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Date getDate(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Time getTime(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}



	public boolean getBoolean(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public byte[] getBytes(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Date getDate(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Time getTime(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return list.getBinaryStream(columnLabel);
	}

	public SQLWarning getWarnings() throws SQLException {
		return list.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		list.clearWarnings();
	}

	public String getCursorName() throws SQLException {
		return list.getCursorName();
	}



	public int findColumn(String columnLabel) throws SQLException {
		return list.findColumn(columnLabel);
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Reader getCharacterStream(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public boolean isBeforeFirst() throws SQLException {
		throw new RuntimeException("not support");
	}

	public boolean isAfterLast() throws SQLException {
		throw new RuntimeException("not support");
	}

	public boolean isFirst() throws SQLException {
		throw new RuntimeException("not support");
	}

	public boolean isLast() throws SQLException {
		throw new RuntimeException("not support");
	}

	public void beforeFirst() throws SQLException {
		throw new RuntimeException("not support");
	}

	public void afterLast() throws SQLException {
		throw new RuntimeException("not support");
	}

	public boolean first() throws SQLException {
		throw new RuntimeException("not support");
	}

	public boolean last() throws SQLException {
		throw new RuntimeException("not support");
	}

	public int getRow() throws SQLException {
		return list.getRow();
	}

	public boolean absolute(int row) throws SQLException {
		return list.absolute(row);
	}

	public boolean relative(int rows) throws SQLException {
		return list.relative(rows);
	}

	public boolean previous() throws SQLException {
		return list.previous();
	}

	public void setFetchDirection(int direction) throws SQLException {
		list.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return list.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		list.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return list.getFetchSize();
	}

	public int getType() throws SQLException {
		return list.getType();
	}

	public int getConcurrency() throws SQLException {
		return list.getConcurrency();
	}

	public void refreshRow() throws SQLException {
	}

	public Statement getStatement() throws SQLException {
		return list.getStatement();
	}

	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Ref getRef(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Blob getBlob(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Clob getClob(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Array getArray(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Ref getRef(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Blob getBlob(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Clob getClob(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Array getArray(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		throw new RuntimeException("not support");
	}

	public URL getURL(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public URL getURL(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public RowId getRowId(int columnIndex) throws SQLException {
		return list.getRowId(columnIndex);
	}

	public RowId getRowId(String columnLabel) throws SQLException {
		return list.getRowId(columnLabel);
	}

	public int getHoldability() throws SQLException {
		return list.getHoldability();
	}

	public boolean isClosed() throws SQLException {
		return list.isClosed();
	}

	public NClob getNClob(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public NClob getNClob(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public String getNString(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public String getNString(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		throw new RuntimeException("not support");
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		throw new RuntimeException("not support");
	}

	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		throw new RuntimeException("not support");
	}

	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		throw new RuntimeException("not support");
	}

	

}
