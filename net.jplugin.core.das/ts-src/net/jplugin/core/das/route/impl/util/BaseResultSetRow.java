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

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;

public abstract class BaseResultSetRow implements ResultSet{
	
	List<Object> data;
	Meta meta;
	
	public static class Meta{
		List<MetaItem> metaList=null;
		/**
		 * colCount，支持只获取ResultSet的前几个字段，忽略后面字段。
		 * @param rsMetaData
		 * @param colCount
		 * @throws SQLException
		 */
		public Meta(ResultSetMetaData rsMetaData, int colCount) throws SQLException {
			metaList = new ArrayList<>(colCount);
			for (int i=0;i<colCount;i++){
				String name = rsMetaData.getColumnName(i+1);
				int type = rsMetaData.getColumnType(i+1);
				this.metaList.add(new MetaItem(name,type));
			}
		}
		
		public Object getColumnTypeName(int columnIndex) throws SQLException {
			throw new RuntimeException("not impl");
		}
		
		public int getColumnType(int columnIndex) throws SQLException {
			if (columnIndex<=0 || columnIndex>metaList.size())
				throw new SQLException("Error columnIndex ."+columnIndex+"  metalen:"+metaList.size());
			return metaList.get(columnIndex-1).getColumnType();
		}
		
		public int getColumnIndex(String columnLabel) throws SQLException {
			for (int i=0;i<metaList.size();i++){
				if (columnLabel.equals(metaList.get(i).getColumnLabel())){
					return i+1;
				}
			}
			throw new SQLException("Can't find column in metadata:"+columnLabel);
		}
		public int size() {
			return this.metaList.size();
		}
	}
	
	static class MetaItem{
		private int columnType;
		String columnLabel;
		
		public MetaItem(String name, int type) {
			this.columnLabel = name;
			this.columnType = type;
		}

		public int getColumnType() {
			return columnType;
		}

		public String getColumnLabel() {
			return columnLabel;
		}
	}
	
	public void clearCurrentRowValue(){
		if (data==null){
			data = new ArrayList(meta.metaList.size());
			for (int i=0;i<this.meta.size();i++){
				data.add(null);
			}
		}else{
			for (int i=0;i<this.meta.size();i++){
				data.set(i, null);
			}
		}
	}
	
	public BaseResultSetRow(Meta m){
		this.meta = m;
	}
	
	protected BaseResultSetRow(ResultSetMetaData rsMetaData) throws SQLException{
		this(rsMetaData,rsMetaData.getColumnCount());
	}
	protected BaseResultSetRow(ResultSetMetaData rsMetaData,int colCount) throws SQLException{
		this.meta = new Meta(rsMetaData,colCount);
	}
	
	public Meta getBaseResultSetRowMeta(){
		return this.meta;
	}
	public List<Object> getBaseResultSetRowData(){
		return this.data;
	}

	protected void setData(List<Object> data) throws SQLException {
		if (data.size()!=this.meta.size()){
			throw new SQLException("Data size must be equal to meta size:"+data.size()+"  "+meta.size());
		}
		this.data = data;
	}
	
	protected List<Object> getData() {
		return data;
	}
	
	public Meta getMeta() {
		return meta;
	}
	
	@Override
	public final <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return SqlTypeKit.get(getData(columnIndex), type);
	}


	@Override
	public final boolean wasNull() throws SQLException {
		throw new SQLException("not impl");
	}

	@Override
	public final String getString(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getString(getData(columnIndex));
	}
	
	private Object getData(int columnIndex){
		return data.get(columnIndex-1);
	}

	@Override
	public final boolean getBoolean(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getBoolean(getData(columnIndex));
	}

	@Override
	public final byte getByte(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getByte(getData(columnIndex));
	}

	@Override
	public final short getShort(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getShort(getData(columnIndex));
	}

	@Override
	public final int getInt(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getInt(getData(columnIndex));
	}

	@Override
	public final long getLong(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getLong(getData(columnIndex));
	}

	@Override
	public final float getFloat(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getFloat(getData(columnIndex));
	}

	@Override
	public final double getDouble(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getDouble(getData(columnIndex));
	}

	@Override  //此方法在接口已经 不推荐使用
	public final BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return SqlTypeKit.service.getBigDecimal(getData(columnIndex));
	}

	@Override
	public final byte[] getBytes(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getBytes(getData(columnIndex));
	}

	@Override
	public final Date getDate(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getDate(getData(columnIndex));
	}

	@Override
	public final Time getTime(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getTime(getData(columnIndex));
	}

	@Override
	public final Timestamp getTimestamp(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getTimestamp(getData(columnIndex));
	}

	@Override
	public final InputStream getAsciiStream(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getAsciiStream(getData(columnIndex));
	}

	@Override
	public final InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getUnicodeStream(getData(columnIndex));
	}

	@Override
	public final InputStream getBinaryStream(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getBinaryStream(getData(columnIndex));
	}

	@Override
	public final Object getObject(int columnIndex) throws SQLException {
		return getData(columnIndex);
	}

	@Override
	public final Reader getCharacterStream(int columnIndex) throws SQLException {
		return new CharArrayReader(getChars(SqlTypeKit.service.getBytes(getData(columnIndex)),"UTF-8"));
	}
	
	@Override
	public final BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return SqlTypeKit.service.getBigDecimal(getData(columnIndex));
	}


	@Override
	public final boolean absolute(int row) throws SQLException {
		throw new SQLException("not impl");
	}

	@Override
	public final boolean relative(int rows) throws SQLException {
		throw new SQLException("not impl");
	}

	@Override
	public final boolean previous() throws SQLException {
		throw new SQLException("not impl");
	}
	

	@Override
	public final int getType() throws SQLException {
		return ResultSet.TYPE_FORWARD_ONLY;
	}

	@Override
	public final int getConcurrency() throws SQLException {
		return ResultSet.CONCUR_READ_ONLY;
	}


	@Override
	public final Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		Class<?> javaClass = map.get(meta.getColumnTypeName(columnIndex));
		if (javaClass == null)
			throw new SQLException("can't find the java type mapping for:" + meta.getColumnTypeName(columnIndex));
		return SqlTypeKit.get(getData(columnIndex), javaClass);
	}

	@Override
	public final Ref getRef(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public final Blob getBlob(int columnIndex) throws SQLException {
		return new SerialBlob(SqlTypeKit.service.getBytes(getData(columnIndex)));
	}

	@Override
	public final Clob getClob(int columnIndex) throws SQLException {
		return new SerialClob(getChars(SqlTypeKit.service.getBytes(getData(columnIndex)),"utf-8"));
	}

	@Override
	public final Array getArray(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}
	@Override
	public final Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return SqlTypeKit.service.getDate(getData(columnIndex));
	}
	@Override
	public final Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return SqlTypeKit.service.getTime(getData(columnIndex));
	}
	@Override
	public final Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return SqlTypeKit.service.getTimestamp(getData(columnIndex));
	}
	@Override
	public final URL getURL(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}


	@Override
	public final Reader getCharacterStream(String columnLabel) throws SQLException {
		return getCharacterStream(findColumn(columnLabel));
	}


	@Override
	public final BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return getBigDecimal(findColumn(columnLabel));
	}

	@Override
	public final Object getObject(String columnLabel) throws SQLException {
		return getObject(findColumn(columnLabel));
	}
	@Override
	public final <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		return getObject(findColumn(columnLabel),type);
	}
	@Override
	public final String getString(String columnLabel) throws SQLException {
		return getString(findColumn(columnLabel));
	}

	@Override
	public final boolean getBoolean(String columnLabel) throws SQLException {
		return getBoolean(findColumn(columnLabel));
	}

	@Override
	public final byte getByte(String columnLabel) throws SQLException {
		return getByte(findColumn(columnLabel));
	}

	@Override
	public final short getShort(String columnLabel) throws SQLException {
		return getShort(findColumn(columnLabel));
	}

	@Override
	public final int getInt(String columnLabel) throws SQLException {
		return getInt(findColumn(columnLabel));
	}

	@Override
	public final long getLong(String columnLabel) throws SQLException {
		return getLong(findColumn(columnLabel));
	}

	@Override
	public final float getFloat(String columnLabel) throws SQLException {
		return getFloat(findColumn(columnLabel));
	}

	@Override
	public final double getDouble(String columnLabel) throws SQLException {
		return getDouble(findColumn(columnLabel));
	}

	@Override
	public final BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return getBigDecimal(findColumn(columnLabel),scale);
	}

	@Override
	public final byte[] getBytes(String columnLabel) throws SQLException {
		return getBytes(findColumn(columnLabel));
	}

	@Override
	public final Date getDate(String columnLabel) throws SQLException {
		return getDate(findColumn(columnLabel));
	}

	@Override
	public final Time getTime(String columnLabel) throws SQLException {
		return getTime(findColumn(columnLabel));
	}

	@Override
	public final Timestamp getTimestamp(String columnLabel) throws SQLException {
		return getTimestamp(findColumn(columnLabel));
	}

	@Override
	public final InputStream getAsciiStream(String columnLabel) throws SQLException {
		return getAsciiStream(findColumn(columnLabel));
	}

	@Override
	public final InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return getUnicodeStream(findColumn(columnLabel));
	}

	@Override
	public final InputStream getBinaryStream(String columnLabel) throws SQLException {
		return getBinaryStream(findColumn(columnLabel));
	}
	@Override
	public final Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		return getObject(findColumn(columnLabel),map);
	}

	@Override
	public final Ref getRef(String columnLabel) throws SQLException {
		return getRef(findColumn(columnLabel));
	}

	@Override
	public final Blob getBlob(String columnLabel) throws SQLException {
		return getBlob(findColumn(columnLabel));
	}

	@Override
	public final Clob getClob(String columnLabel) throws SQLException {
		return getClob(findColumn(columnLabel));
	}

	@Override
	public final Array getArray(String columnLabel) throws SQLException {
		return getArray(findColumn(columnLabel));
	}



	@Override
	public final Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return getDate(findColumn(columnLabel),cal);
	}



	@Override
	public final Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return getTime(findColumn(columnLabel),cal);
	}



	@Override
	public final Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		return getTimestamp(findColumn(columnLabel),cal);
	}



	@Override
	public final URL getURL(String columnLabel) throws SQLException {
		return getURL(findColumn(columnLabel));
	}

	public final int findColumn(String columnLabel) throws SQLException {
		int lb = meta.getColumnIndex(columnLabel);
		if (lb < 0)
			throw new SQLException("Can't find column by name:" + columnLabel);

		return lb;
	}

	@Override
	public final RowId getRowId(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public final RowId getRowId(String columnLabel) throws SQLException {
		throw new SQLException("not support");
	}

	

	@Override
	public final int getHoldability() throws SQLException {
		throw new SQLException("not support");
	}
	

	@Override
	public final NClob getNClob(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public final NClob getNClob(String columnLabel) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public final SQLXML getSQLXML(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public final SQLXML getSQLXML(String columnLabel) throws SQLException {
		throw new SQLException("not support");
	}
	
	@Override
	public final Reader getNCharacterStream(String columnLabel) throws SQLException {
		throw new SQLException("not support");
	}


	@Override
	public final String getNString(int columnIndex) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public final String getNString(String columnLabel) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public final Reader getNCharacterStream(int columnIndex) throws SQLException {
		throw new SQLException("not support");
//		byte[] v = SqlDataTypeTransformer.commonTransformer.getBytes(getData(columnIndex));
//		return new CharArrayReader(getChars(v, "UTF-16"));
	}


	
	@Override
	public final boolean rowUpdated() throws SQLException {
		return false;
	}

	@Override
	public final boolean rowInserted() throws SQLException {
		return false;
	}

	@Override
	public final boolean rowDeleted() throws SQLException {
		return false;
	}

	@Override
	public final void updateNull(int columnIndex) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBoolean(int columnIndex, boolean x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateByte(int columnIndex, byte x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateShort(int columnIndex, short x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateInt(int columnIndex, int x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateLong(int columnIndex, long x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateFloat(int columnIndex, float x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateDouble(int columnIndex, double x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateString(int columnIndex, String x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBytes(int columnIndex, byte[] x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateDate(int columnIndex, Date x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateTime(int columnIndex, Time x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateObject(int columnIndex, Object x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNull(String columnLabel) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBoolean(String columnLabel, boolean x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateByte(String columnLabel, byte x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateShort(String columnLabel, short x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateInt(String columnLabel, int x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateLong(String columnLabel, long x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateFloat(String columnLabel, float x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateDouble(String columnLabel, double x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateString(String columnLabel, String x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBytes(String columnLabel, byte[] x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateDate(String columnLabel, Date x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateTime(String columnLabel, Time x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateObject(String columnLabel, Object x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}



	@Override
	public final void refreshRow() throws SQLException {
	}

	@Override
	public final void cancelRowUpdates() throws SQLException {
	}

	@Override
	public final void moveToInsertRow() throws SQLException {
	}

	@Override
	public final void moveToCurrentRow() throws SQLException {
	}
	
	@Override
	public void setFetchSize(int rows) throws SQLException {
	}
	
	@Override
	public int getFetchSize() throws SQLException {
		return 1;
	}
	
	@Override
	public final void updateRef(int columnIndex, Ref x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateRef(String columnLabel, Ref x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBlob(String columnLabel, Blob x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateClob(int columnIndex, Clob x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateClob(String columnLabel, Clob x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateArray(int columnIndex, Array x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateArray(String columnLabel, Array x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	
	@Override
	public final void updateNString(int columnIndex, String nString) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateRowId(int columnIndex, RowId x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateRowId(String columnLabel, RowId x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}
	@Override
	public final void updateNString(String columnLabel, String nString) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		throw new RuntimeException("now support operation.");
	}
	@Override
	public final void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateClob(int columnIndex, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateClob(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNClob(int columnIndex, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateNClob(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("now support operation.");
	}
	

	@Override
	public boolean first() throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final boolean last() throws SQLException {
		throw new RuntimeException("now support operation.");
	}
	@Override
	public final int getFetchDirection() throws SQLException {
		return ResultSet.FETCH_FORWARD;
	}
	@Override
	public final void setFetchDirection(int direction) throws SQLException {
		if (direction!=ResultSet.FETCH_FORWARD)
			throw new SQLException("not support :"+direction);
	}

	@Override
	public final void insertRow() throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void updateRow() throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void deleteRow() throws SQLException {
		throw new RuntimeException("now support operation.");
	}
	
	@Override
	public final void beforeFirst() throws SQLException {
		throw new RuntimeException("now support operation.");
	}

	@Override
	public final void afterLast() throws SQLException {
		throw new RuntimeException("now support operation.");
	}
	@Override
	public boolean isFirst() throws SQLException {
		throw new SQLException("not impl");
	}

	@Override
	public boolean isLast() throws SQLException {
		throw new SQLException("not impl");
	}
	@Override
	public int getRow() throws SQLException {
		throw new SQLException("not impl");
	}

	private char[] getChars(byte[] bytes,String charset) {
		Charset cs = Charset.forName(charset);
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		return cb.array();
	}

}
