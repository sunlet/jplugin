package net.jplugin.core.das.route.impl.conn.mulqry;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.TablesplitException;

public class CombinedSqlParser {
	public static final String SPANALL_DATASOURCE = "##SPANALL##";
	/**
	 * {..jsoninfo...}##SELECT ....
	 * 
	 * @param sql
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
	public static class Meta{
		private String sourceTb;
		private DataSourceInfo[] dataSourceInfos;
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
		
	}

}
