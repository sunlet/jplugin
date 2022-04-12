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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.jplugin.core.das.dds.kits.EmptyQueryableResultSet;
import net.jplugin.core.das.route.impl.util.BaseResultSetRow;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * 
 * <pre>
 * 实现方法：
 * 跨表查询，有groupby时候不能有orderby，然后系统把groupby的内容作为orderby。
 * 因为如果有orderby，和groupby不一致的时候，会导致查询到多条重复groupby的记录。
 * 添加一个隐含字段。f__g__b__o ,内容为相关字段的CONCAT.
 * </pre>
 * @author LiuHang
 *
 */
public class GroupByWrapper extends BaseResultSetRow{
	ResultSet inner;
	private int columnCount;
	
	/**
	 * 实际字段比可用字段少2个
	 * @param l
	 * @throws SQLException
	 */
	public GroupByWrapper(ResultSet l,List<SelectItem> initialItems) throws SQLException{
		super(l.getMetaData(),l.getMetaData().getColumnCount()-2);
		this.columnCount = l.getMetaData().getColumnCount()-2;//实际记录集的行数
		this.inner = l;
		initExpressionAggrator(initialItems);
	}

	long count = -1;
	boolean beforeFirst = true;//还没有next过
	boolean afterLast = false;//已经next到最后了
	boolean currentIsLastRow = false;//当前是最后一行
	
	List<ExpressionAggregrator> aggregrateList;
	
	private void initExpressionAggrator(List<SelectItem> initialItems) {
		int size = initialItems.size();
		aggregrateList = new ArrayList(size);
		for (int i=0;i<size;i++){
			aggregrateList.add(new ExpressionAggregrator(initialItems.get(i)));
		}
	}
	/**
	 * 用最后一个字段分组
	 * @return
	 * @throws SQLException
	 */
	public boolean next() throws SQLException {
		clearCurrentRowValue();
		clearExpressionAggregateStatus();

		//是否最后一行
		if (currentIsLastRow){
			this.afterLast = true;
			return false;
		}
		//是否先next一次
		if (this.beforeFirst){
			boolean b = inner.next();
			if (!b) return false;
		}
		
		//到这里可以确定beforeFirst为false了
		this.beforeFirst = false;
		
		//必然return true
		String  currentGroupKey = inner.getString(columnCount+1);
		fetchValue();
		
		while(inner.next()){
			String temp = inner.getString(columnCount+1);
			if (equal(temp,currentGroupKey)){
				fetchValue();
			}else{
				//更新改行数据
				initCurrentRowData();
				
				//返回true
				return true;
			}
		}
		//仍然返回true，但是这已经是最后一条
		currentIsLastRow = true;
		//更新改行数据
		initCurrentRowData();
		
		return true;
	}

	private void initCurrentRowData() throws SQLException {
		int size = aggregrateList.size();
		List<Object> coldata = super.getBaseResultSetRowData();
		for (int i=0;i<size;i++){
			Object result = aggregrateList.get(i).getResult(super.getMeta().getColumnType(i+1));
			coldata.set(i, result);
		}
		
	}
	private void clearExpressionAggregateStatus() {
		int size = aggregrateList.size();
		for (int i=0;i<size;i++){
			this.aggregrateList.get(i).resetState();
		}
	}
	private boolean equal(String a, String b) {
		if  (a==null){
			if (b==null) return true;
			else return false;
		}else{
			if (a.equals(b)) return true;
			else return false;
		}
	}

	private void fetchValue() throws SQLException {
		int valueCnt = inner.getInt(this.columnCount+2);//最后一个字段为count
		
		for (int i=0;i<this.columnCount;i++){
			Object v = inner.getObject(i+1);
			this.aggregrateList.get(i).aggrateItem(v,valueCnt,super.getMeta().getColumnType(i+1));
		}
	}

	@Override
	public void close() throws SQLException {
		inner.close();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
	}

	@Override
	public String getCursorName() throws SQLException {
		return inner.getCursorName();
	}

	@Override
	public Statement getStatement() throws SQLException {
		return inner.getStatement();
	}
	
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return new TruncedMetaDataAdaptor(inner.getMetaData(),this.columnCount);
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return this.beforeFirst;
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		return this.afterLast;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return inner.isClosed();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return inner.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return inner.isWrapperFor(iface);
	}
	
	static class TruncedMetaDataAdaptor implements ResultSetMetaData{
		
		private ResultSetMetaData meta;
		private int colCount;

		public TruncedMetaDataAdaptor(ResultSetMetaData metaData, int acolCount) {
			this.meta = metaData;
			this.colCount = acolCount;
		}

		public int getColumnCount() throws SQLException {
			return this.colCount;
		}

		//以下方法都是adaptor方法
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return meta.unwrap(iface);
		}

		public boolean isAutoIncrement(int column) throws SQLException {
			return meta.isAutoIncrement(column);
		}

		public boolean isCaseSensitive(int column) throws SQLException {
			return meta.isCaseSensitive(column);
		}

		public boolean isSearchable(int column) throws SQLException {
			return meta.isSearchable(column);
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return meta.isWrapperFor(iface);
		}

		public boolean isCurrency(int column) throws SQLException {
			return meta.isCurrency(column);
		}

		public int isNullable(int column) throws SQLException {
			return meta.isNullable(column);
		}

		public boolean isSigned(int column) throws SQLException {
			return meta.isSigned(column);
		}

		public int getColumnDisplaySize(int column) throws SQLException {
			return meta.getColumnDisplaySize(column);
		}

		public String getColumnLabel(int column) throws SQLException {
			return meta.getColumnLabel(column);
		}

		public String getColumnName(int column) throws SQLException {
			return meta.getColumnName(column);
		}

		public String getSchemaName(int column) throws SQLException {
			return meta.getSchemaName(column);
		}

		public int getPrecision(int column) throws SQLException {
			return meta.getPrecision(column);
		}

		public int getScale(int column) throws SQLException {
			return meta.getScale(column);
		}

		public String getTableName(int column) throws SQLException {
			return meta.getTableName(column);
		}

		public String getCatalogName(int column) throws SQLException {
			return meta.getCatalogName(column);
		}

		public int getColumnType(int column) throws SQLException {
			return meta.getColumnType(column);
		}

		public String getColumnTypeName(int column) throws SQLException {
			return meta.getColumnTypeName(column);
		}

		public boolean isReadOnly(int column) throws SQLException {
			return meta.isReadOnly(column);
		}

		public boolean isWritable(int column) throws SQLException {
			return meta.isWritable(column);
		}

		public boolean isDefinitelyWritable(int column) throws SQLException {
			return meta.isDefinitelyWritable(column);
		}

		public String getColumnClassName(int column) throws SQLException {
			return meta.getColumnClassName(column);
		}
		
		
	}

}
