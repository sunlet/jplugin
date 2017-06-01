package net.jplugin.mtenant.impl.kit.util;

import java.util.List;
import java.util.Map;

/**
 * @author gaojh
 * @date 2017/1/7
 */
public class SqlHelper {

	public static String format(String sourceSql) {

		sourceSql = StringUtils.replaceAll(sourceSql, "\\t", " ");
		sourceSql = StringUtils.replaceAll(sourceSql, "\\r", " ");
		sourceSql = StringUtils.replaceAll(sourceSql, "\\n", " ");
		sourceSql = StringUtils.replaceAll(sourceSql, "= ", "=");
		sourceSql = StringUtils.replaceAll(sourceSql, " =", "=");

		sourceSql = StringUtils.replaceAll(sourceSql, "\\(", " ( ");
		sourceSql = StringUtils.replaceAll(sourceSql, "\\)", " ) ");

		sourceSql = StringUtils.replaceAll(sourceSql, ",", " , ");
		while (StringUtils.contains(sourceSql, "  ")) {
			sourceSql = StringUtils.replaceAll(sourceSql, "  ", " ");
		}

		return sourceSql.toLowerCase().trim();

	}

	public static String toSql(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (String s : list) {
			if (checkFunctionStr(s)) {
				sb.append(s);
			} else {
				sb.append(s).append(" ");
			}
		}
		String sql = sb.toString().trim();
		while (true) {
			if (StringUtils.contains(sql, "  ")) {
				sql = StringUtils.replaceAll(sql, "  ", " ");
			} else {
				break;
			}
		}
		sql = StringUtils.replaceAll(sql, "\\( ", "(");
		sql = StringUtils.replaceAll(sql, " \\)", ")");
		sql = StringUtils.replaceAll(sql, " \\(", "(");
		sql = StringUtils.replaceAll(sql, " , ", ", ");

		if (sql.startsWith("select")) {
			sql = StringUtils.replaceAll(sql, " in\\(", " in (");
		}

		if (sql.startsWith("insert")) {
			sql = StringUtils.replaceAll(sql, " values\\(", " values (");
		}

		if (sql.startsWith("delete")) {
			sql = StringUtils.replaceAll(sql, " in\\(", " in (");
		}

		if (sql.startsWith("update")) {
			sql = StringUtils.replaceAll(sql, " in\\(", " in (");
		}
		return sql;
	}

	public static String createWhere(Map<String, Object> params, String flag) {
		StringBuilder sb = new StringBuilder();
		if ("N".equalsIgnoreCase(flag)) {
			sb.append("where ");
		}
		for (String key : params.keySet()) {
			Object value = params.get(key);
			if (value instanceof Integer) {
				sb.append(key).append("=").append(value).append(" and ");
			} else if (value instanceof Long) {
				sb.append(key).append("=").append(value).append(" and ");
			} else if (value instanceof Float) {
				sb.append(key).append("=").append(value).append(" and ");
			} else if (value instanceof Double) {
				sb.append(key).append("=").append(value).append(" and ");
			} else {
				sb.append(key).append("='").append(value).append("' and ");
			}
		}

		if ("N".equalsIgnoreCase(flag)) {
			return StringUtils.substringBeforeLast(sb.toString(), " and ");
		} else {
			return sb.toString();
		}
	}

	public static String createWheres(Map<String, String> aliasmap,
			List<String> tables, Map<String, Object> params, String flag) {
		if (tables == null || tables.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if ("N".equalsIgnoreCase(flag)) {
			sb.append("where ");
		}

		for (String t : tables) {
			for (String key : params.keySet()) {
				Object value = params.get(key);
				if (value instanceof Integer || value instanceof Long
						|| value instanceof Float || value instanceof Double) {
					if (aliasmap.containsKey(t)) {
						sb.append(aliasmap.get(t)).append(".").append(key)
								.append("=").append(value).append(" and ");
					} else {
						sb.append(key).append("=").append(value)
								.append(" and ");
					}
				} else {
					if (aliasmap.containsKey(t)) {
						sb.append(aliasmap.get(t)).append(".").append(key)
								.append("='").append(value).append("' and ");
					} else {
						sb.append(key).append("='").append(value)
								.append("' and ");
					}
				}
			}
		}

		if ("N".equalsIgnoreCase(flag)) {
			return StringUtils.substringBeforeLast(sb.toString(), " and ");
		} else {
			return sb.toString();
		}
	}

	public static boolean checkFunctionStr(String str) {
		if (str.equals("avg") || str.equals("count") || str.equals("first")
				|| str.equals("last") || str.equals("max") || str.equals("min")
				|| str.equals("sum") || str.equals("ucase")
				|| str.equals("lcase") || str.equals("mid")
				|| str.equals("len") || str.equals("round")
				|| str.equals("now") || str.equals("format")) {
			return true;
		} else {
			return false;
		}
	}
}
