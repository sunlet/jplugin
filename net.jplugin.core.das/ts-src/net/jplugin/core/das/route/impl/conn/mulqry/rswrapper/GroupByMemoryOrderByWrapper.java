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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.Comparor;
import net.jplugin.common.kits.SortUtil;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.config.api.RefConfig;
import net.jplugin.core.das.route.impl.CombinedSelectContext;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.GroupByMemoryOrderByWrapper.RowComparor;
import net.jplugin.core.das.route.impl.util.BaseResultSetRow;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.OrderByElement;

/**
 * 内存排序
 * @author LiuHang
 *
 */
public class GroupByMemoryOrderByWrapper extends BaseResultSetRow{

	GroupByWrapper inner;
	List<List<Object>> dataGrid;//数据列表
	private List<OrderByInfo> initialOrderBy;//最初的orderby列表
	RowComparor rowComparor = new RowComparor();
	
	int currentRowIndex = -1; //当前数据行号
	boolean beforeFirst = true;
	boolean afterLast = false;
	private ResultSetMetaData resultSetMetaData;
	private boolean isClosed;//是否关闭
	
	public GroupByMemoryOrderByWrapper(GroupByWrapper rs, List<OrderByElement> oldOrderBy) throws SQLException {
		super(rs.getBaseResultSetRowMeta());
		this.inner = rs;
		this.initialOrderBy = parseOrderBy(oldOrderBy);
		this.resultSetMetaData = rs.getMetaData();
		initData();
	}
	private List<OrderByInfo> parseOrderBy(List<OrderByElement> list) throws SQLException {
		ArrayList temp = new ArrayList<>(list.size());
		for (OrderByElement obe:list){
			Expression exp = obe.getExpression();
			if (!(exp instanceof Column)){
				throw new SQLException("the order by must be a column. "+exp.toString() +" sql="+CombinedSelectContext.get().getOriginalSql());
			}
			OrderByInfo o = new OrderByInfo();
			o.fieldName = ((Column)exp).getColumnName();
			o.isDesc = !obe.isAsc();
			int idx = super.getMeta().getColumnIndex(o.fieldName);
			if (idx<=0){
				throw new SQLException("Can't find the order by column in sql. "+o.fieldName+"  sql="+CombinedSelectContext.get().getOriginalSql());
			}
			o.columnIndex = idx;
			temp.add(o);
		}
		return temp;
	}
	static int rowLimit;
	int getRowLimit(){
		if (rowLimit>0) {
			return rowLimit;
		}else{
			synchronized (this.getClass()) {
				if (rowLimit<=0)
					rowLimit = ConfigFactory.getIntConfig("platform.route-sql-memory-order-limit",2000);
				return rowLimit;
			}
		}
	}
	
	private void initData() {
		try {
			dataGrid = new ArrayList<List<Object>>();
			try{
				int rowCnt = 0;
				while(inner.next()){
					rowCnt++;
					if (rowCnt>getRowLimit())
						throw new RuntimeException("platform.route-sql-memory-order-limit overflow."+CombinedSelectContext.get().getFinalSql());
					
					List<Object> row = inner.getBaseResultSetRowData();
					List<Object> temp = new ArrayList(row.size());
					temp.addAll(row);
					dataGrid.add(temp);
				}
			}finally{
				try {
					this.inner.close();
				}catch(Exception e){}
			}
			//排序一下
			SortUtil.sort(dataGrid,this.rowComparor );
		} catch (SQLException e) {
			throw new RuntimeException(CombinedSelectContext.get().getFinalSql(),e);
		}
	}
	

	@Override
	public boolean next() throws SQLException {
		if (this.afterLast) 
			return false;

		clearRowData();
		this.beforeFirst = false;
		this.currentRowIndex ++; //刚开始时从 -1 增加到 0
		
		if (this.currentRowIndex >=this.dataGrid.size()){
			this.afterLast = true;
			return false;
		}else{
			initRowData(this.currentRowIndex);
			return true;
		}
	}
	private void initRowData(int idx) throws SQLException {
		super.setData(this.dataGrid.get(idx));
	}
	private void clearRowData() {
		super.clearCurrentRowValue();
	}
	@Override
	public void close() throws SQLException {
		this.isClosed= true;
		if (this.dataGrid!=null) 
			this.dataGrid.clear();
		this.dataGrid = null;
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
		return null;
	}
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return this.resultSetMetaData;
	}
	@Override
	public boolean isBeforeFirst() throws SQLException {
		return this.isBeforeFirst();
	}
	@Override
	public boolean isAfterLast() throws SQLException {
		return this.afterLast;
	}
	@Override
	public Statement getStatement() throws SQLException {
		throw new SQLException("not support");
	}
	@Override
	public boolean isClosed() throws SQLException {
		return isClosed;
	}
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
	
	static class OrderByInfo{
		public int columnIndex;
		String fieldName;
		boolean isDesc;
	}
	
	class RowComparor implements Comparor {
		@Override
		public boolean isGreaterThen(Object o1, Object o2) {
			List<Object> list1 = (List<Object>) o1;
			List<Object> list2 = (List<Object>) o2;
			
			for (OrderByInfo obi:initialOrderBy){
				Object v1 = list1.get(obi.columnIndex-1);
				Object v2 = list2.get(obi.columnIndex-1);
				
				//null作为最大值
				if (v2==null)
					return false;
				if (v1==null){ 
					return true;
				}
				
				//比较结果
				int result = ((Comparable)v1).compareTo(v2);
				if (result>0){
					return obi.isDesc? false:true;
				}
				if (result<0)
					return obi.isDesc? true:false;
			}
			return false;
		}
	}
}
