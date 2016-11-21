package net.jplugin.core.das.route.impl.conn.mulqry;

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
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.Meta;
import net.jplugin.core.das.route.impl.conn.mulqry.ResultSetOrderByTool.OrderComparor;

/**
 * 实现一个类型为 ResultSet.TYPE_FORWARD_ONLY的ResultSet
 * @author Administrator
 *
 */
public class ResultSetList extends EmptyQueryableResultSet implements ResultSet{
	private List<ResultSet> list = new ArrayList<ResultSet>();
	
	private ResultSet o=null;
	private OrderComparor orderComparorOfO = null;
	
	private Statement stmt=null;
	private boolean closed;
	ResultSetOrderByTool orderByTool;

	private ResultSetMetaData metadata;
	
	
	ResultSetList(Statement s, List<ResultSet> rsList, List<String> orderParam){
		this.stmt = s;
		this.list.addAll(rsList);
		prepareFetch(orderParam);
	}
	
	public List<ResultSet> getList(){
		return this.list;
	}
	public void add(ResultSet resultSet) {
		list.add(resultSet);
	}
	private void prepareFetch(List<String> orderParam) {
		if (list.size()==0) 
			throw new RuntimeException("list size is 0");
		this.orderByTool = new ResultSetOrderByTool(orderParam,list.get(0));
		
		for (int i=0;i<list.size();i++){
			ResultSet rs = list.get(i);
			try{
				if (rs.next()){
					OrderComparor oc= new OrderComparor();
					oc.setRsIndex(i);
					this.orderByTool.refreshAndAdd(oc, rs);
				}
			}catch(SQLException e){
				throw new TablesplitException(e);
			}
		}
		
		
	}
	
	@Override
	public void setFetchDirection(int direction) throws SQLException {
		if (direction!= FETCH_FORWARD) 
			throw new RuntimeException("Not support");
		else{
			//do nothing ,不设置
		}
	}
	@Override
	public int getFetchDirection() throws SQLException {
		return FETCH_FORWARD;
	}
	@Override
	public void setFetchSize(int rows) throws SQLException {
		//这里用super实现也没什么问题
		o.setFetchSize(rows);
	}
	@Override
	public int getFetchSize() throws SQLException {
		return o.getFetchSize();
	}
	@Override
	public int getType() throws SQLException {
		return ResultSet.TYPE_FORWARD_ONLY;
	}
	@Override
	public int getConcurrency() throws SQLException {
		return CONCUR_READ_ONLY;
	}
	@Override
	public void refreshRow() throws SQLException {
		o.refreshRow();
	}
	@Override
	public Statement getStatement() throws SQLException {
		return stmt;
	}
	
	@Override
	public boolean next() throws SQLException {

		if (o==null){
			//直接取出来一个排在第一位的
			return retriveCurrent();
		}else{
			//把当前的next以后，放入排序队列
			if (o.next()){
				this.orderByTool.refreshAndAdd(orderComparorOfO, o);
			}

			//再取出来一个放在第一位的
			return retriveCurrent();
		}
	}
	
	//该方法一定会设置o和orderComparorOfO
	private boolean retriveCurrent() {
		OrderComparor first = this.orderByTool.pollFirst();
		if (first ==null){
			//把当前的o清理掉
			o = null;
			orderComparorOfO = null;
			//这一个为null
			return false;
		}else{
			//设置第一个o 和 orderComparorOfO
			o = this.list.get(first.getRsIndex());
			orderComparorOfO = first;
			return true;
		}
	}
	@Override
	public void close() throws SQLException {
		Exception th=null;
		this.closed = true;
		for (ResultSet rs:list){
			try{
				rs.close();
			}catch(Exception ex){
				th = ex;
			}
		}
		if (th!=null) {
			if (th instanceof SQLException){
				throw (SQLException)th;
			}else {
				throw new SQLException(th.getMessage(),th);
			}
		}
	}
	@Override
	public boolean isClosed() throws SQLException {
		return this.closed;
	}
	public ResultSetMetaData getMetaData() throws SQLException {
		if (metadata==null)
			metadata = list.get(0).getMetaData();
		return metadata;
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		throw new RuntimeException("Not support");
	}
	@Override
	public boolean isAfterLast() throws SQLException {
		throw new RuntimeException("Not support");
	}
	@Override
	public boolean isFirst() throws SQLException {
		throw new RuntimeException("Not support");
	}
	@Override
	public boolean isLast() throws SQLException {
		throw new RuntimeException("Not support");
	}
	@Override
	public void beforeFirst() throws SQLException {
		throw new RuntimeException("Not support");
	}
	@Override
	public void afterLast() throws SQLException {
		throw new RuntimeException("Not support");
	}
	@Override
	public boolean first() throws SQLException {
		throw new RuntimeException("Not support");
	}
	@Override
	public boolean last() throws SQLException {
		throw new RuntimeException("Not support");
	}

	@Override
	public int getRow() throws SQLException {
		throw new RuntimeException("Not support");
	}
	@Override
	public boolean absolute(int row) throws SQLException {
		throw new SQLFeatureNotSupportedException("not support");
	}
	@Override
	public boolean relative(int rows) throws SQLException {
		throw new SQLFeatureNotSupportedException("not support");
	}
	@Override
	public boolean previous() throws SQLException {
		throw new SQLFeatureNotSupportedException("not support");
	}
	
	//以下为默认实现
	
	public boolean wasNull() throws SQLException {
		return o.wasNull();
	}
	public String getString(int columnIndex) throws SQLException {
		return o.getString(columnIndex);
	}
	public boolean getBoolean(int columnIndex) throws SQLException {
		return o.getBoolean(columnIndex);
	}
	public byte getByte(int columnIndex) throws SQLException {
		return o.getByte(columnIndex);
	}
	public short getShort(int columnIndex) throws SQLException {
		return o.getShort(columnIndex);
	}
	public int getInt(int columnIndex) throws SQLException {
		return o.getInt(columnIndex);
	}
	public long getLong(int columnIndex) throws SQLException {
		return o.getLong(columnIndex);
	}
	public float getFloat(int columnIndex) throws SQLException {
		return o.getFloat(columnIndex);
	}
	public double getDouble(int columnIndex) throws SQLException {
		return o.getDouble(columnIndex);
	}
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return o.getBigDecimal(columnIndex, scale);
	}
	public byte[] getBytes(int columnIndex) throws SQLException {
		return o.getBytes(columnIndex);
	}
	public Date getDate(int columnIndex) throws SQLException {
		return o.getDate(columnIndex);
	}
	public Time getTime(int columnIndex) throws SQLException {
		return o.getTime(columnIndex);
	}
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return o.getTimestamp(columnIndex);
	}
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return o.getAsciiStream(columnIndex);
	}
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return o.getUnicodeStream(columnIndex);
	}
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return o.getBinaryStream(columnIndex);
	}
	public String getString(String columnLabel) throws SQLException {
		return o.getString(columnLabel);
	}
	public boolean getBoolean(String columnLabel) throws SQLException {
		return o.getBoolean(columnLabel);
	}
	public byte getByte(String columnLabel) throws SQLException {
		return o.getByte(columnLabel);
	}
	public short getShort(String columnLabel) throws SQLException {
		return o.getShort(columnLabel);
	}
	public int getInt(String columnLabel) throws SQLException {
		return o.getInt(columnLabel);
	}
	public long getLong(String columnLabel) throws SQLException {
		return o.getLong(columnLabel);
	}
	public float getFloat(String columnLabel) throws SQLException {
		return o.getFloat(columnLabel);
	}
	public double getDouble(String columnLabel) throws SQLException {
		return o.getDouble(columnLabel);
	}
	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return o.getBigDecimal(columnLabel, scale);
	}
	public byte[] getBytes(String columnLabel) throws SQLException {
		return o.getBytes(columnLabel);
	}
	public Date getDate(String columnLabel) throws SQLException {
		return o.getDate(columnLabel);
	}
	public Time getTime(String columnLabel) throws SQLException {
		return o.getTime(columnLabel);
	}
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return o.getTimestamp(columnLabel);
	}
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return o.getAsciiStream(columnLabel);
	}
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return o.getUnicodeStream(columnLabel);
	}
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return o.getBinaryStream(columnLabel);
	}
	public SQLWarning getWarnings() throws SQLException {
		return o.getWarnings();
	}
	public void clearWarnings() throws SQLException {
		o.clearWarnings();
	}
	public String getCursorName() throws SQLException {
		return o.getCursorName();
	}
	public Object getObject(int columnIndex) throws SQLException {
		return o.getObject(columnIndex);
	}
	public Object getObject(String columnLabel) throws SQLException {
		return o.getObject(columnLabel);
	}
	public int findColumn(String columnLabel) throws SQLException {
		return o.findColumn(columnLabel);
	}
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return o.getCharacterStream(columnIndex);
	}
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return o.getCharacterStream(columnLabel);
	}
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return o.getBigDecimal(columnIndex);
	}
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return o.getBigDecimal(columnLabel);
	}
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		return o.getObject(columnIndex, map);
	}
	public Ref getRef(int columnIndex) throws SQLException {
		return o.getRef(columnIndex);
	}
	public Blob getBlob(int columnIndex) throws SQLException {
		return o.getBlob(columnIndex);
	}
	public Clob getClob(int columnIndex) throws SQLException {
		return o.getClob(columnIndex);
	}
	public Array getArray(int columnIndex) throws SQLException {
		return o.getArray(columnIndex);
	}
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		return o.getObject(columnLabel, map);
	}
	public Ref getRef(String columnLabel) throws SQLException {
		return o.getRef(columnLabel);
	}
	public Blob getBlob(String columnLabel) throws SQLException {
		return o.getBlob(columnLabel);
	}
	public Clob getClob(String columnLabel) throws SQLException {
		return o.getClob(columnLabel);
	}
	public Array getArray(String columnLabel) throws SQLException {
		return o.getArray(columnLabel);
	}
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return o.getDate(columnIndex, cal);
	}
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return o.getDate(columnLabel, cal);
	}
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return o.getTime(columnIndex, cal);
	}
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return o.getTime(columnLabel, cal);
	}
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return o.getTimestamp(columnIndex, cal);
	}
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		return o.getTimestamp(columnLabel, cal);
	}
	public URL getURL(int columnIndex) throws SQLException {
		return o.getURL(columnIndex);
	}
	public URL getURL(String columnLabel) throws SQLException {
		return o.getURL(columnLabel);
	}
	public RowId getRowId(int columnIndex) throws SQLException {
		return o.getRowId(columnIndex);
	}
	public RowId getRowId(String columnLabel) throws SQLException {
		return o.getRowId(columnLabel);
	}
	public int getHoldability() throws SQLException {
		return o.getHoldability();
	}
	public NClob getNClob(int columnIndex) throws SQLException {
		return o.getNClob(columnIndex);
	}
	public NClob getNClob(String columnLabel) throws SQLException {
		return o.getNClob(columnLabel);
	}
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return o.getSQLXML(columnIndex);
	}
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return o.getSQLXML(columnLabel);
	}
	public String getNString(int columnIndex) throws SQLException {
		return o.getNString(columnIndex);
	}
	public String getNString(String columnLabel) throws SQLException {
		return o.getNString(columnLabel);
	}
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return o.getNCharacterStream(columnIndex);
	}
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return o.getNCharacterStream(columnLabel);
	}
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return o.getObject(columnIndex, type);
	}
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		return o.getObject(columnLabel, type);
	}

}
