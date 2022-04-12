package net.jplugin.core.das.route.api;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.dds.api.AbstractRouterDataSource;
import net.jplugin.core.das.dds.api.IRouterDataSource;
import net.jplugin.core.das.dds.api.TablesplitException;
import net.jplugin.core.das.route.impl.autocreate.TableExistsMaintainer;
import net.jplugin.core.das.route.impl.autocreate.TableExistsMaintainer.MaintainReturn;
import net.jplugin.core.das.route.impl.conn.LogUtil;
import net.jplugin.core.das.route.impl.conn.SpecialReturnHandler;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.conn.mulqry.CombineStatementFactory;

/**
 * 仅仅是一个代理的数据源，不能setAutoCommit，不能commit、不能roolback、不能close，都无效
 * @author Administrator
 *
 */
public class RouterDataSource extends AbstractRouterDataSource{

	PrintWriter logWriter;
	RouterDataSourceConfig config = new RouterDataSourceConfig();
	private String dataSourceName;
	
//	public RouterDataSource(String dsname) {
//		this.dataSourceName = dsname;
//	}
	
	public String getDataSourceName(){
		return this.dataSourceName;
	}

//	public void config(Map<String,String> cfg){
//		config.fromProperties(cfg);
//	}
	
	public RouterDataSourceConfig getConfig() {
		return config;
	}
	
	
	@Override
	public void init(String routerDataSourceName,Map<String, String> c) {
		this.dataSourceName = routerDataSourceName;
		config.fromProperties(c);
	}

	@Override
	public Connection getTargetConnBefConnect() {
		return null;
	}

	@Override
	public PreparedStatement getTargetPreparedStmtBefCreate(IRouterDataSource routeDatasource,String sql) {
		/**
		 * 如果匹配到,则返回null,否则返回默认数据源
		 */
		if (checkSqlMatched(sql))
			return null;
		else
			return createDefaultPreparedStatement(sql);
	}



	@Override
	public PreparedStatement getTargetPreparedStmtBefExecute(IRouterDataSource routeDatasource,String sql, List<Object> params) throws SQLException {
		if (sql == null)
			throw new TablesplitException("No sql found");
		return computePreparedStatementResult(routeDatasource, sql, params);
	}

	@Override
	public StatementResult getTargetStmtBefExecute(IRouterDataSource routeDatasource,String sql) throws SQLException {
		if (sql==null) throw new TablesplitException("No sql found");
		
		/**
		 * 如果匹配到,则返回分库分表内容,  ,否则返回默认数据源
		 */
		if (checkSqlMatched(sql)) {
			return computeStatementResult(routeDatasource, sql);
		}else {
			return createDefaultStatementResult(sql);
		}
	}
	
	private StatementResult createDefaultStatementResult(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean checkSqlMatched(String sql) {
		return true;
	}

	private PreparedStatement createDefaultPreparedStatement(String sql) {
		return null;
	}

	private PreparedStatement computePreparedStatementResult(IRouterDataSource routeDatasource, String sql,
			List<Object> params) throws SQLException {
		SqlHandleResult shr = SqlHandleService.INSTANCE.handle((RouterDataSource) routeDatasource, sql, params);
		LogUtil.instance.log(shr);
		
		//处理无存在的表的特殊情况
		String  dsForMakeDymmy = shr.getDataSourceInfos()[0].getDsName();
		MaintainReturn maintainResult = TableExistsMaintainer.maintainAndCheckNoneResult(shr);
		if (maintainResult.isSpecialCondition()){
			PreparedStatement temp = SpecialReturnHandler.hanleSpecialConditionForPreparedStmt(maintainResult,DataSourceFactory.getDataSource(dsForMakeDymmy).getConnection());
//			this.executeResult.set(temp);
			return temp;
		}
		
//		String targetDataSourceName = shr.getTargetDataSourceName();
		PreparedStatement stmt ;
//		if (CombinedSqlParser.SPAN_DATASOURCE.equals(targetDataSourceName)){
		if (!shr.singleTable()){
			stmt = CombineStatementFactory.createPrepared(shr.getEncodedSql());
		}else{
			String dsname = shr.getDataSourceInfos()[0].getDsName();
			DataSource tds = DataSourceFactory.getDataSource(dsname);
			if (tds == null)
				throw new TablesplitException("Can't find target datasource." + dsname);
			stmt = tds.getConnection().prepareStatement(shr.getResultSql());
		}
//		executeResult.set(stmt);
//		list.executeWith(stmt);
		return stmt;
	}
	
	private StatementResult computeStatementResult(IRouterDataSource routeDatasource, String sql) throws SQLException {
		SqlHandleResult shr = SqlHandleService.INSTANCE.handle((RouterDataSource) routeDatasource,sql);
		
		LogUtil.instance.log(shr);
		
		//处理无存在的表的特殊情况
		String  dsForMakeDymmy = shr.getDataSourceInfos()[0].getDsName();
		MaintainReturn maintainResult = TableExistsMaintainer.maintainAndCheckNoneResult(shr);
		if (maintainResult.isSpecialCondition()){
			StatementResult temp = SpecialReturnHandler.hanleSpecialConditionForStatement(maintainResult,DataSourceFactory.getDataSource(dsForMakeDymmy).getConnection());
//			this.executeResult.set(temp.statement);
			return temp;
		}
		
		Statement stmt;
		//根据不同状态获取不同的statement
		if (!shr.singleTable()){
			stmt = CombineStatementFactory.create();
		}else{
			String dsname = shr.getDataSourceInfos()[0].getDsName();
			DataSource tds = DataSourceFactory.getDataSource(dsname);
			if (tds==null) 
				throw new TablesplitException("Can't find target datasource."+dsname);
			stmt = tds.getConnection().createStatement();
		}
		StatementResult result = new StatementResult();
		result.setStatement(stmt); //result.statement = stmt;
//		this.executeResult.set(result.statement);
		
		//根据不同状态使用不同的sql
		if (shr.singleTable())
//			result.resultSql = shr.resultSql;
			result.setResultSql(shr.getResultSql());
		else
//			result.resultSql = shr.getEncodedSql();
			result.setResultSql(shr.getEncodedSql());
		return result;
	}

	@Override
	public PreparedStatement getTargetPreparedStmtBefCreate(IRouterDataSource routeDatasource, String sql,
			int autoGenKeys) throws SQLException {
		throw new SQLException("NOT SUPPORT");
	}
}
