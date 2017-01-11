package net.jplugin.core.mtenant.impl.kit.parser.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import net.jplugin.core.mtenant.impl.kit.parser.SqlParser;
import net.jplugin.core.mtenant.impl.kit.utils.SqlHelper;
import net.jplugin.core.mtenant.impl.kit.utils.StringUtils;

/**
 * @author gaojh
 * @date 2017/1/8
 */
public class DeleteSqlParser implements SqlParser {
	@Override
	public String parse(String sourceSql, Map<String, Object> params, List<String> ignoreTables) {
		if (params.isEmpty()) {
			return sourceSql;
		}

		String sql = SqlHelper.format(sourceSql);
		if (!StringUtils.startsWith(sql, "delete")) {
			return sql;
		}

		String[] sqlArray = StringUtils.split(sql, " ");
		if (ignoreTables == null) {
			ignoreTables = new ArrayList<>();
		}

		int length = sqlArray.length;
		TreeMap<Integer, String> map = new TreeMap<>();
		for (int i = 0; i < length; i++) {
			if ("from".equals(sqlArray[i])) {
				String table = sqlArray[i + 1];
				if (ignoreTables.contains(table)) {
					continue;
				}
				if (i + 2 == length) {
					// delete from table
					map.put(i + 2, "N");
				} else if ("where".equals(sqlArray[i + 2])) {
					map.put(i + 3, "Y");
				} else {
					// select * from table )
					map.put(i + 2, "N");
				}
			}
		}

		if (map.size() == 0) {
			return sourceSql;
		}
		NavigableMap<Integer, String> nmap = map.descendingMap();
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList(sqlArray));

		for (int i : nmap.keySet()) {
			if (i + 1 <= list.size()) {
				list.add(i, SqlHelper.createWhere(params, nmap.get(i)));
			} else {
				list.add(SqlHelper.createWhere(params, nmap.get(i)));
			}
		}

		return SqlHelper.toSql(list);
	}

}
