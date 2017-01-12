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

public class SelectSqlParser implements SqlParser {

	@Override
	public String parse(String sourceSql, Map<String, Object> params, List<String> ignoreTables) {

		if (params.isEmpty()) {
			return sourceSql;
		}

		String[] sqlArray = StringUtils.split(sourceSql, " ");
		if (ignoreTables == null) {
			ignoreTables = new ArrayList<>();
		}

		int length = sqlArray.length;
		TreeMap<Integer, String> map = new TreeMap<>();
		for (int i = 0; i < length; i++) {
			if ("from".equals(sqlArray[i])) {
				if (i + 1 == length) {
					// select * from
					throw new RuntimeException("error sql: " + sourceSql);
				} else {
					String table = sqlArray[i + 1];
					if (ignoreTables.contains(table) || "(".equals(table)) {
						continue;
					}
					if (i + 2 == length) {
						// select * from table
						map.put(i + 2, "N");
					} else if ("where".equals(sqlArray[i + 2])) {
						map.put(i + 3, "Y");
					} else {
						// select * from table )
						map.put(i + 2, "N");
					}
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
