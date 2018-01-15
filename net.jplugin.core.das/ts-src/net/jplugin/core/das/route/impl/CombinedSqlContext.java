package net.jplugin.core.das.route.impl;

import net.jplugin.common.kits.AttributedObject;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Limit;

public class CombinedSqlContext extends AttributedObject {
	private static final String COMBINED_SQL_CONTEXT = "COMBINED_SQL_CONTEXT";

	private String originalTableName;
	/**
	 * 数据源列表
	 */
	private DataSourceInfo[] dataSourceInfos;
	/**
	 * 最原始的sql
	 */
	private String originalSql;
	/**
	 * statement，最终的，中间如果要修改直接修改。最后拿statement生成 finalSql
	 */
	private Statement statement;
	/**
	 * 最后的sql
	 */
	private String finalSql;

	public static CombinedSqlContext get(){
			ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
			return (CombinedSqlContext) ctx.getAttribute(COMBINED_SQL_CONTEXT);
	}
	public static void set(CombinedSqlContext o) {
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		ctx.setAttribute(COMBINED_SQL_CONTEXT,o);
	}

	public String getOriginalSql() {
		return originalSql;
	}

	public void setOriginalSql(String originalSql) {
		this.originalSql = originalSql;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public String getFinalSql() {
		return finalSql;
	}

	public void setFinalSql(String finalSql) {
		this.finalSql = finalSql;
	}

	public DataSourceInfo[] getDataSourceInfos() {
		return dataSourceInfos;
	}
	public void setDataSourceInfos(DataSourceInfo[] dataSourceInfos) {
		this.dataSourceInfos = dataSourceInfos;
	}
	public String getOriginalTableName() {
		return originalTableName;
	}
	public void setOriginalTableName(String originalTableName) {
		this.originalTableName = originalTableName;
	}
	
}
