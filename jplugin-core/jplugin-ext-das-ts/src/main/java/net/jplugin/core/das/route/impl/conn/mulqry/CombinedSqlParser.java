package net.jplugin.core.das.route.impl.conn.mulqry;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.core.das.dds.api.TablesplitException;
import net.jplugin.core.das.dds.kits.SqlParserKit;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.impl.CombinedSelectContext;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.WrapperManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

public class CombinedSqlParser {
	public static final String SPAN_DATASOURCE = "##SPAN##";
	/**
	 * {..jsoninfo...}##SELECT ....
	 * 
	 * @param sql
	 * @return 
	 * @return 
	 */
	public static ParseResult parse(String combinedSql) {
		int pos = combinedSql.indexOf("##");
		if (pos<0) throw new TablesplitException("not a combined sql:"+combinedSql);
		String json = combinedSql.substring(0,pos);
		String sql = combinedSql.substring(pos+2);
		ParseResult pr = new ParseResult();
		pr.sql = sql;
		pr.meta = JsonKit.json2Object(json, Meta.class);
		
		//初始化sql执行的上下文环境
//		return makeContext(pr);
		return pr;
	}
	
	public static String combine(String sql,Meta meta){
		String json = JsonKit.object2Json(meta);
		return json + "##"+sql;
	}

	public static class ParseResult {
		private String sql;
		private Meta meta;
		public String getSql() {
			return sql;
		}
		public Meta getMeta() {
			return meta;
		}
	}
	
//	private static CombinedSelectContext makeContext(ParseResult pr) {
//		CombinedSelectContext ctx = new CombinedSelectContext();
//		String originalSql = pr.getSql();
//		ctx.setOriginalSql(originalSql);
//		ctx.setDataSourceInfos(pr.getMeta().getDataSourceInfos());
//		ctx.setOriginalTableName(pr.getMeta().getSourceTb());
//		
//		Statement statement = SqlParserKit.parse(originalSql);
////		try {
////			CCJSqlParserManager pm = new CCJSqlParserManager();
//////			pm.parse(new StringReader(originalSql));
//////			CCJSqlParser parser = new CCJSqlParser(new StringReader(originalSql));
////			statement =   pm.parse(new StringReader(originalSql));
////		} catch (Exception e) {
////			throw new RuntimeException("sql parse error:"+originalSql);
////		}
//		
//		ctx.setStatement((Select) statement);
//		WrapperManager.INSTANCE.handleContextInitial(ctx);
//		ctx.setFinalSql(ctx.getStatement().toString());//设置最终sql
//		
//		//设置
//		CombinedSelectContext.set(ctx);
//		return ctx;
//	}
	public static class Meta{
//		public static final int COUNG_STAR_YES = 1;
//		public static final int COUNG_STAR_NO = 0;
		
		private String sourceTb;
		private DataSourceInfo[] dataSourceInfos;

//		private List<String> orderParam;
//		int countStar;
		
//		public int getCountStar() {
//			return countStar;
//		}
//		public void setCountStar(int countStar) {
//			this.countStar = countStar;
//		}
		public String getSourceTb() {
			return sourceTb;
		}
		public void setSourceTb(String sourceTb) {
			this.sourceTb = sourceTb;
		}
		public DataSourceInfo[] getDataSourceInfos() {
			return dataSourceInfos;
		}
		public void setDataSourceInfos(DataSourceInfo[] dataSourceInfos) {
			this.dataSourceInfos = dataSourceInfos;
		}
//		public List<String> getOrderParam() {
//			return orderParam;
//		}
//		public void setOrderParam(List<String> orderParam) {
//			this.orderParam = orderParam;
//		}

	}

	public static class OrderParam{
		String col;
		String direInfo;
		public String getCol() {
			return col;
		}
		public void setCol(String col) {
			this.col = col;
		}
		public String getDireInfo() {
			return direInfo;
		}
		public void setDireInfo(String direInfo) {
			this.direInfo = direInfo;
		}
	}
}
