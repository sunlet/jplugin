package net.jplugin.core.das.route.impl;

import net.jplugin.common.kits.AttributedObject;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.ParseResult;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.WrapperManager;
import net.jplugin.core.das.route.impl.util.SqlParserKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.Select;

public class CombinedSelectContext extends AttributedObject {
	private static final String COMBINED_SELECT_CONTEXT = "COMBINED_SELECT_CONTEXT";

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
	private Select statement;
	/**
	 * 最后的sql
	 */
	private String finalSql;

	
	public static CombinedSelectContext makeContext(ParseResult pr) {
		CombinedSelectContext ctx = new CombinedSelectContext();
		String originalSql = pr.getSql();
		ctx.setOriginalSql(originalSql);
		ctx.setDataSourceInfos(pr.getMeta().getDataSourceInfos());
		ctx.setOriginalTableName(pr.getMeta().getSourceTb());
		
		Statement statement = SqlParserKit.parse(originalSql);
//		try {
//			CCJSqlParserManager pm = new CCJSqlParserManager();
////			pm.parse(new StringReader(originalSql));
////			CCJSqlParser parser = new CCJSqlParser(new StringReader(originalSql));
//			statement =   pm.parse(new StringReader(originalSql));
//		} catch (Exception e) {
//			throw new RuntimeException("sql parse error:"+originalSql);
//		}
		
		ctx.setStatement((Select) statement);
		WrapperManager.INSTANCE.handleContextInitial(ctx);
		ctx.setFinalSql(ctx.getStatement().toString());//设置最终sql
		
		//设置
		CombinedSelectContext.set(ctx);
		return ctx;
	}
	
	public static CombinedSelectContext get(){
			ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
			return (CombinedSelectContext) ctx.getAttribute(COMBINED_SELECT_CONTEXT);
	}
	public static void set(CombinedSelectContext o) {
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		ctx.setAttribute(COMBINED_SELECT_CONTEXT,o);
	}

	public String getOriginalSql() {
		return originalSql;
	}

	public void setOriginalSql(String originalSql) {
		this.originalSql = originalSql;
	}

	public Select getStatement() {
		return statement;
	}

	public void setStatement(Select statement) {
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
