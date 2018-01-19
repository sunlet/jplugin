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
import net.jplugin.core.das.route.impl.CombinedSqlContext;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.GroupByMemoryOrderByWrapper.RowComparor;
import net.jplugin.core.das.route.impl.util.BaseResultSetRow;
import net.sf.jsqlparser.statement.select.OrderByElement;

/**
 * 内存排序
 * @author LiuHang
 *
 */
public class GroupByMemoryOrderByWrapper extends BaseResultSetRow{
	public class RowComparor implements Comparor {
		@Override
		public boolean isGreaterThen(Object o1, Object o2) {
			List<Object> list1 = (List<Object>) o1;
			List<Object> list2 = (List<Object>) o2;
			//TODO ....
			return false;
		}
	}
	GroupByWrapper inner;
	List<List<Object>> dataGrid;
	public GroupByMemoryOrderByWrapper(GroupByWrapper rs, List<OrderByElement> oldOrderBy) {
		super(rs.getBaseResultSetRowMeta());
		this.inner = rs;
		initData();
	}
	private void initData() {
		try {
			dataGrid = new ArrayList<List<Object>>();
			while(inner.next()){
				List<Object> row = inner.getBaseResultSetRowData();
				dataGrid.add(row);
			}
			//排序一下
			SortUtil.sort(dataGrid, new RowComparor());
		} catch (SQLException e) {
			throw new RuntimeException(CombinedSqlContext.get().getFinalSql(),e);
		}
	}
	
	@Override
	public boolean next() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		
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
	public Statement getStatement() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
}
