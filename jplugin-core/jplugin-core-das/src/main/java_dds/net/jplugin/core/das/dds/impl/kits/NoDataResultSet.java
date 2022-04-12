package net.jplugin.core.das.dds.impl.kits;

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

public class NoDataResultSet extends EmptyQueryableResultSet{

		public static final ResultSet INSTANCE = new NoDataResultSet();

		@Override
		public boolean next() throws SQLException {
			return false;
		}

		@Override
		public void close() throws SQLException {
		}

		@Override
		public boolean wasNull() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getString(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean getBoolean(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
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
		public String getString(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean getBoolean(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public byte getByte(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public short getShort(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getInt(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getLong(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float getFloat(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double getDouble(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public byte[] getBytes(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Date getDate(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Time getTime(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Timestamp getTimestamp(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public InputStream getAsciiStream(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public InputStream getUnicodeStream(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public InputStream getBinaryStream(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SQLWarning getWarnings() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void clearWarnings() throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getCursorName() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResultSetMetaData getMetaData() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getObject(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getObject(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int findColumn(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Reader getCharacterStream(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Reader getCharacterStream(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isBeforeFirst() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isAfterLast() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isFirst() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isLast() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void beforeFirst() throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterLast() throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean first() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean last() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getRow() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
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
		public void setFetchDirection(int direction) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getFetchDirection() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setFetchSize(int rows) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getFetchSize() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
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
		public void refreshRow() throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Statement getStatement() throws SQLException {
			// TODO Auto-generated method stub
			return null;
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
		public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Ref getRef(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Blob getBlob(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Clob getClob(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Array getArray(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Date getDate(int columnIndex, Calendar cal) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Date getDate(String columnLabel, Calendar cal) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Time getTime(int columnIndex, Calendar cal) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Time getTime(String columnLabel, Calendar cal) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public URL getURL(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public URL getURL(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public RowId getRowId(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public RowId getRowId(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getHoldability() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isClosed() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public NClob getNClob(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public NClob getNClob(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SQLXML getSQLXML(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SQLXML getSQLXML(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getNString(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getNString(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Reader getNCharacterStream(int columnIndex) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Reader getNCharacterStream(String columnLabel) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
