package net.jplugin.core.mtenant.impl.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.mtenant.impl.kit.parser.SqlParser;
import net.jplugin.core.mtenant.impl.kit.parser.impl.DeleteSqlParser;
import net.jplugin.core.mtenant.impl.kit.parser.impl.InsertSqlParser;
import net.jplugin.core.mtenant.impl.kit.parser.impl.SelectSqlParser;
import net.jplugin.core.mtenant.impl.kit.parser.impl.UpdateSqlParser;
import net.jplugin.core.mtenant.impl.kit.utils.SqlHelper;
import net.jplugin.core.mtenant.impl.kit.utils.StringUtils;

public class SqlMultiTenantHanlderKit {
	/**
	 * @param dataSourceName
	 * @param sql
	 * @return
	 */
	private static ConcurrentHashMap<String, List<String>> ignores = null;
	private static Logger logger = LogFactory.getLogger(SqlMultiTenantHanlderKit.class);

	public static String handle(String dataSourceName, String sql) {
		String result = handleInner(dataSourceName, sql);
		if (logger.isDebugEnabled()){
			if (!sql.equals(result)){
				logger.debug("BeforeSQL = "+sql);
				logger.debug("AfterSQL = "+result);
			}
		}
		return result;
	}
	public static String handleInner(String dataSourceName, String sql) {
		if ("false".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable", "FALSE"))) {
			return sql;
		}

		String datasource = ConfigFactory.getStringConfig("mtenant.datasource", "ALL");
		String[] datasources = StringUtils.split(datasource.trim(), ",");

		if (!"ALL".equalsIgnoreCase(datasource) && !Arrays.asList(datasources).contains(dataSourceName)) {
			return sql;
		}

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

		sql = SqlHelper.format(sql);
		if (StringUtils.contains(sql, "ignore-tenant")) {
			return sql;
		}

		String tenantId = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		if (tenantId == null || tenantId.trim().length() == 0) {
			throw new IllegalArgumentException("tenantId is empty");
		}

		ConcurrentHashMap<String, Object> params = new ConcurrentHashMap<>();
		params.put(ConfigFactory.getStringConfig("mtenant.field"), tenantId);

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
		return parser.parse(sql, params, ignores.get(dataSourceName));
	}
}
