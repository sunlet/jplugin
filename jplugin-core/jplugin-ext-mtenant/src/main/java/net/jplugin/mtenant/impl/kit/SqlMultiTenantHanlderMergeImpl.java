package net.jplugin.mtenant.impl.kit;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.api.sqlrefactor.ISqlRefactor;
import net.jplugin.core.das.dds.impl.DummyConnection;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.mtenant.impl.kit.parse.SqlParser;
import net.jplugin.mtenant.impl.kit.parse.impl.DeleteSqlParser;
import net.jplugin.mtenant.impl.kit.parse.impl.InsertSqlParser;
import net.jplugin.mtenant.impl.kit.parse.impl.SelectSqlParser;
import net.jplugin.mtenant.impl.kit.parse.impl.UpdateSqlParser;
import net.jplugin.mtenant.impl.kit.util.StringUtils;

public class SqlMultiTenantHanlderMergeImpl implements ISqlRefactor{
	/**
	 * @param dataSourceName
	 * @param sql
	 * @return
	 */
	private static ConcurrentHashMap<String, List<String>> ignores = null;
	private static Logger logger = LogFactory.getLogger(SqlMultiTenantHanlderMergeImpl.class);

	
	public  String refactSql(String dataSourceName, String sql,Connection conn) {

		String result = handleInner(dataSourceName, sql,conn);
		if (logger.isDebugEnabled()){
			if (!sql.equals(result)){
				logger.debug("BeforeSQL = "+sql);
				logger.debug("AfterSQL = "+result);
			}
		}
		return result;
	}
	public  String handleInner(String dataSourceName, String sql,Connection conn) {
		if ("false".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable", "FALSE"))) {
			return sql;
		}

		String datasource = ConfigFactory.getStringConfig("mtenant.datasource", "ALL");
		String[] datasources = StringUtils.split(datasource.trim(), ",");

		if (!"ALL".equalsIgnoreCase(datasource) && !Arrays.asList(datasources).contains(dataSourceName)) {
			return sql;
		}
		
		//router connection数据源不能配置为多租户
		boolean isRouter =false;
		try{
			isRouter = conn.isWrapperFor(DummyConnection.class);
		}catch(Exception e){
			throw new RuntimeException("Error while call isWrapper",e);
		}
		if (isRouter) 
			throw new RuntimeException("Router connection can't be configed with multinant."+conn.getClass().getName());

		

		if (ignores == null) {
			ignores = new ConcurrentHashMap<>();
			if (!"ALL".equalsIgnoreCase(datasource)) {
				for (String s : datasources) {
					String exclude = ConfigFactory.getStringConfig("mtenant.datasource." + s + ".exclude");
					if (exclude != null) {
						String[] excludes = StringUtils.split(exclude, ",");
						List<String> list = new ArrayList<>();
						for (String t : excludes) {
							list.add(t);
						}
						ignores.put(s, list);
					}
				}
			} else {
				String exclude = ConfigFactory.getStringConfig("mtenant.datasource." + dataSourceName + ".exclude");
				if (exclude != null) {
					String[] excludes = StringUtils.split(exclude, ",");
					List<String> list = new ArrayList<>();
					for (String t : excludes) {
						list.add(t);
					}
					ignores.put(dataSourceName, list);
				}
			}
		} else {
			if ("ALL".equalsIgnoreCase(datasource) || StringUtils.contains(datasource, dataSourceName)) {
				if (!ignores.containsKey(dataSourceName)) {
					String exclude = ConfigFactory.getStringConfig("mtenant.datasource." + dataSourceName + ".exclude");
					if (exclude != null) {
						String[] excludes = StringUtils.split(exclude, ",");
						List<String> list = new ArrayList<>();
						for (String t : excludes) {
							list.add(t);
						}
						ignores.put(dataSourceName, list);
					}
				}
			}
		}

		if (StringUtils.contains(sql, "ignore-tenant")) {
			return sql;
		}

		String tenantId = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		if (tenantId == null || tenantId.trim().length() == 0) {
			throw new IllegalArgumentException("tenantId is empty");
		}

		ConcurrentHashMap<String, Object> params = new ConcurrentHashMap<>();
		params.put(ConfigFactory.getStringConfig("mtenant.field"), tenantId);

		sql = sql.toLowerCase().trim();
		SqlParser parser;
		if (StringUtils.startsWithIgnoreCase(sql, "select")) {
			parser = new SelectSqlParser();
		} else if (StringUtils.startsWithIgnoreCase(sql, "update")) {
			parser = new UpdateSqlParser();
		} else if (StringUtils.startsWithIgnoreCase(sql, "insert")) {
			parser = new InsertSqlParser();
		} else if (StringUtils.startsWithIgnoreCase(sql, "delete")) {
			parser = new DeleteSqlParser();
		} else {
			return sql;
		}
		String dealSql = parser.parse(sql, params, ignores.get(dataSourceName));
		
		/*List<String> ignoreTables = ignores.get(dataSourceName);
		boolean havePlatform = false;
		for(Map.Entry<String, Object> entry : params.entrySet()){
			if(dealSql.contains(entry.getKey())){
				havePlatform=true;
				break;
			}
		}
		boolean haveIgnore=false;
		if(!havePlatform){
			for(String ignore: ignoreTables){
				if(dealSql.contains(ignore)){
					haveIgnore=true;
					break;
				}
			}
		}
		if(!haveIgnore&&!havePlatform){
			throw new IllegalArgumentException("SQL parse error");
		}*/
		return dealSql;
	}

}
