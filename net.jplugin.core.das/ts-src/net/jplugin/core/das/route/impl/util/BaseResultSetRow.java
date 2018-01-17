package net.jplugin.core.das.route.impl.util;

import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
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
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public abstract class BaseResultSetRow implements ResultSet{
	
	List<Object> data ;
	List<String> columnNames;
	
	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return SqlTypeKit.get(data.get(columnIndex), type);
	}


	@Override
	public boolean wasNull() throws SQLException {
		return false;
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return false;
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean absolute(int row) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean previous() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public int getType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public URL getURL(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return getCharacterStream(findColumn(columnLabel));
	}


	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return getBigDecimal(findColumn(columnLabel));
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		return getObject(findColumn(columnLabel));
	}
	@Override
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		return getObject(findColumn(columnLabel),type);
	}
	@Override
	public String getString(String columnLabel) throws SQLException {
		return getString(findColumn(columnLabel));
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return getBoolean(findColumn(columnLabel));
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		return getByte(findColumn(columnLabel));
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		return getShort(findColumn(columnLabel));
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		return getInt(findColumn(columnLabel));
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		return getLong(findColumn(columnLabel));
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return getFloat(findColumn(columnLabel));
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return getDouble(findColumn(columnLabel));
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return getBigDecimal(findColumn(columnLabel),scale);
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		return getBytes(findColumn(columnLabel));
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return getDate(findColumn(columnLabel));
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		return getTime(findColumn(columnLabel));
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return getTimestamp(findColumn(columnLabel));
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return getAsciiStream(findColumn(columnLabel));
	}

	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return getUnicodeStream(findColumn(columnLabel));
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return getBinaryStream(findColumn(columnLabel));
	}
	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		return getObject(findColumn(columnLabel),map);
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		return getRef(findColumn(columnLabel));
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		return getBlob(findColumn(columnLabel));
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		return getClob(findColumn(columnLabel));
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		return getArray(findColumn(columnLabel));
	}



	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return getDate(findColumn(columnLabel),cal);
	}



	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return getTime(findColumn(columnLabel),cal);
	}



	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		return getTimestamp(findColumn(columnLabel),cal);
	}



	@Override
	public URL getURL(String columnLabel) throws SQLException {
		return getURL(findColumn(columnLabel));
	}

	public int findColumn(String columnLabel) throws SQLException {
		int lb = columnNames.indexOf(columnLabel);
		if (lb < 0)
			throw new SQLException("Can't find column by name:" + columnLabel);

		return lb;
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		throw new SQLException("not support");
	}

	

	@Override
	public int getHoldability() throws SQLException {
		throw new SQLException("not support");
	}
	

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		throw new SQLException("not support");
	}
	
	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		throw new SQLException("not support");
	}


	@Override
	public String getNString(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		throw new SQLException("not support");
//		byte[] v = SqlDataTypeTransformer.commonTransformer.getBytes(data.get(columnIndex));
//		return new CharArrayReader(getChars(v, "UTF-16"));
	}
	
	@SuppressWarnings("unused")
	private char[] getChars(byte[] bytes,String charset) {
		Charset cs = Charset.forName(charset);
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		return cb.array();
	}
	
	@Override
	public boolean rowUpdated() throws SQLException {
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		return false;
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}



	@Override
	public void refreshRow() throws SQLException {
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
	}

	@Override
	public void moveToInsertRow() throws SQLException {
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
	}
	
	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	
	@Override
	public void updateNString(int columnIndex, String nString) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}
	@Override
	public void updateNString(String columnLabel, String nString) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		throw new RuntimeException("now support operation.");
	}
	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}


}
