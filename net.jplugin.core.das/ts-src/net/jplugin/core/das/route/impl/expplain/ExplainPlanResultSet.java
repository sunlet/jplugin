package net.jplugin.core.das.route.impl.expplain;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.util.BaseResultSetRow;

/**
 * 未完成实现，貌似需要实现ResultSetMeta接口啊，哈哈
 * @author Administrator
 *
 */
public class ExplainPlanResultSet extends BaseResultSetRow{

	private List<List<Object>> datas;
	private int position = -1;

	public ExplainPlanResultSet(SqlHandleResult shr){
		this.getMeta().addItem("id",Types.INTEGER);
		this.getMeta().addItem("select_type",Types.VARCHAR);
		this.getMeta().addItem("table",Types.VARCHAR);
		this.getMeta().addItem("type",Types.VARCHAR);
		this.getMeta().addItem("possible_keys",Types.VARCHAR);
		this.getMeta().addItem("key",Types.VARCHAR);
		this.getMeta().addItem("key_len",Types.SMALLINT);
		this.getMeta().addItem("ref",Types.VARCHAR);
		this.getMeta().addItem("rows",Types.INTEGER);
		this.getMeta().addItem("Extra",Types.VARCHAR);
		
		DataMaker dataMaker= new DataMaker();
//		if (CombinedSqlParser.SPAN_DATASOURCE.equals(shr.getTargetDataSourceName())){
//			ParseResult result = CombinedSqlParser.parse(shr.getResultSql());
//			DataSourceInfo[] infos = result.getMeta().getDataSourceInfos();
//			for (DataSourceInfo dsinfo:infos){
//				String[] tables = dsinfo.getDestTbs();
//				String dsname = dsinfo.getDsName();
//				for (String t:tables){
//					dataMaker.addData(dsname, t);
//				}
//			}
//		}else{
//			dataMaker.addData(shr.getTargetDataSourceName(), shr.getTargetTable());
//		}
		
		this.datas = dataMaker.getDatas();
	}
	
	static class DataMaker{
		List<List<Object>> datas = new ArrayList();
		int index=1;
		public void addData(String dsname,String tbname){
			datas.add(makeRow(dsname,tbname));
		}
		private List<Object> makeRow(String dsname, String tbname) {
			List<Object> row = new ArrayList();
			row.add(index++);
			row.add(null);
			row.add(tbname);
			row.add(null);
			row.add(null);
			row.add(null);
			row.add(null);
			row.add(null);
			row.add(null);
			row.add(dsname);
			return row;
		}
		public List<List<Object>> getDatas() {
			return datas;
		}
	}
	

	@Override
	public boolean next() throws SQLException {
		if (position<this.datas.size()){
			position++;
		}
		
		if (position<this.datas.size()){
			this.setData(this.datas.get(position));
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void close() throws SQLException {
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
//		return this.reu;
		throw new RuntimeException("not inpl, message");
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
