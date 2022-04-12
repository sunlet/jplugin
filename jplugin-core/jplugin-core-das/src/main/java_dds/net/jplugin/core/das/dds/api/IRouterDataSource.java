package net.jplugin.core.das.dds.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import net.jplugin.core.das.dds.impl.DummyConnection;

/**
 * <pre>
   *  四个方法的实现规则：getTargetConnBefConnect,getTargetPreparedStmtBefCreate,getTargetPreparedStmtBefExecute,getTargetStmtBefExecute
 *1.如果getTargetConnBefConnect返回非空，则其他三个方法不用实现。
 *2.如果getTargetConnBefConnect返回空
 *2.1 getTargetStmtBefExecute需要实现
 *2.2 如果getTargetPreparedStmtBefCreate返回非空，则getTargetPreparedStmtBefExecute不需要实现，否则getTargetPreparedStmtBefExecute需要实现
 * </pre>
 * @author LiuHang
 *
 */

public interface IRouterDataSource {

	public abstract void init(String dataSoruceName,Map<String,String> config);
	
	/**
	 * 如果返回非空,则不会调用后续方法
	 * @return
	 */
	public abstract Connection getTargetConnBefConnect() throws SQLException;
	
	/**
	 * getTargetConnBefConnect 返回空时会调用这个方法
	 *  如果返回非空，则不会调用getTargetPreparedStmtBefExecute，否则会调用
	 * @param sql
	 * @return
	 */
	public abstract PreparedStatement getTargetPreparedStmtBefCreate(IRouterDataSource routeDatasource,String sql) throws SQLException;


	public abstract PreparedStatement getTargetPreparedStmtBefCreate(IRouterDataSource routeDatasource,String sql,int autoGenKeys) throws SQLException;

	
	/**
	 * getTargetPreparedStmtBefCreate返回空时会调用这个方法
	 * @param sql
	 * @param params
	 * @return
	 */
	public abstract PreparedStatement getTargetPreparedStmtBefExecute(IRouterDataSource routeDatasource,String sql,List<Object> params) throws SQLException;
	
	/**
	 * getTargetConnBefConnect 返回空时会调用这个方法
	 * @param sql
	 * @return
	 */
	public abstract StatementResult getTargetStmtBefExecute(IRouterDataSource routeDatasource,String sql) throws SQLException;
	
	
	
	
	public static class StatementResult{
		Statement statement;
		String resultSql;
		
		public static StatementResult with(Statement stmt,String sql) {
			StatementResult o = new StatementResult();
			o.statement = stmt;
			o.resultSql = sql;
			return o;
		}
		
		public Statement getStatement() {
			return statement;
		}
		public void setStatement(Statement statement) {
			this.statement = statement;
		}
		public String getResultSql() {
			return resultSql;
		}
		public void setResultSql(String resultSql) {
			this.resultSql = resultSql;
		}
	
	}
}
