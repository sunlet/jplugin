package net.jplugin.core.das.dds.dsnamed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.dds.api.IConnectionSettable;
import net.jplugin.core.das.dds.api.IRouterDataSource.StatementResult;
import net.jplugin.core.das.dds.impl.EmptyStatement;
import net.jplugin.core.das.dds.impl.kits.SchemaCheckKit;
import net.jplugin.core.das.dds.kits.SqlParserKit;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;

public class InsertSelectStatement extends EmptyStatement implements Statement,IConnectionSettable{
	private String theSql;
	
	private String insertDataSource;
	private String selectDataSource;
	private int updateCount;
	private Connection connection;
	private Insert insert;
	private Select select;

	
	public static void main(String[] args) throws SQLException {
//		String sql = "insert into tb1 (f1,f2,f3)  select a,b,c from tb2";
//		String sql = "insert into tb1 (f1,f2,f3)  values (?,?,?)";
//		net.sf.jsqlparser.statement.Statement stmt = SqlParserKit.parse(sql);
//		System.out.println(stmt );
		
		String sql = "insert into db1.tb1 (f1,f2,f3)  select a,b,c from db2.tb2";
		InsertSelectStatement.create(sql);
	}
	
	public static StatementResult create(String sql) throws SQLException {
		InsertSelectStatement o = new InsertSelectStatement(sql);
		return StatementResult.with(o, sql);
	}
	
	public InsertSelectStatement(String sql) throws SQLException {
		this.theSql = sql;
		insert = (Insert) SqlParserKit.parse(sql);
		select = insert.getSelect();
		
		//去除select
		insert.setSelect(null);
		
		
		Set<String> schemaForInsert = SchemaCheckKit.extractSchema(insert,false);
		Set<String> schemaForSelect = SchemaCheckKit.extractSchema(select,false);
		
		if (schemaForInsert.size()!=1) {
			throw new SQLException("Only one schema is allowed for insert in the insert-select sql:"+sql);
		}
		this.insertDataSource = schemaForInsert.iterator().next();
		SchemaCheckKit.extractAndRemoveSchema(insert,false);
		
		if (schemaForSelect.size()!=1) {
			throw new SQLException("Only one schema is allowed for select in the insert-select sql:"+sql);
		}
		this.selectDataSource = schemaForSelect.iterator().next();
		SchemaCheckKit.extractAndRemoveSchema(select,false);
	}

	private static void refactInsert(Insert insert,int columnCnt) {
//		int columnCnt = insert.getColumns().size();
		insert.setSelect(null);
		ExpressionList expList = new ExpressionList();
		insert.setItemsList(expList);
		
		expList.setExpressions(new ArrayList());
		for (int i=0;i<columnCnt;i++)
			expList.getExpressions().add(new JdbcParameter());
		
		insert.setUseValues(true);
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		return executeInner(sql);
	}

	private int executeInner(String sql) throws SQLException {
		AssertKit.assertTrue(sql==this.theSql);//这里肯定是同一个sql，因为是执行时才生成的
		
		Statement selectStmt = DataSourceFactory.getDataSource(this.selectDataSource).getConnection().createStatement();
		ResultSet resultSet = selectStmt.executeQuery(this.select.toString());

		int columnCnt = resultSet.getMetaData().getColumnCount();
		//修改insert语句,增加values(?,?,?)
		refactInsert(this.insert,columnCnt);
		PreparedStatement insertStmt = DataSourceFactory.getDataSource(this.insertDataSource).getConnection().prepareStatement(this.insert.toString());
		
		int cnt = 0;
		while(resultSet.next()) {
			for (int i=1;i<=columnCnt;i++) {
				Object o = resultSet.getObject(i);
				insertStmt.setObject(i, o);
			}
			insertStmt.executeUpdate();
			cnt++;
		}
		this.updateCount = cnt;
		return cnt;
	}

	@Override
	public void close() throws SQLException {
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		executeInner(sql);
		return false;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return this.updateCount;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.connection;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw new SQLException("NOT SUPPORT");
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw new SQLException("NOT SUPPORT");
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw new SQLException("NOT SUPPORT");
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw new SQLException("NOT SUPPORT");
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw new SQLException("not support");
	}

	@Override
	public boolean isClosed() throws SQLException {
		return false;
	}

	@Override
	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		throw new SQLException("not support");
	}

}
