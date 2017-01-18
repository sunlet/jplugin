package net.jplugin.core.mtenant.impl.kit.parser.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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


	        String sql = SqlHelper.format(sourceSql);
	        if (!StringUtils.startsWith(sql, "select")) {
	            return sql;
	        }

	        String[] sqlArray = StringUtils.split(sql, " ");
	        if (ignoreTables == null) {
	            ignoreTables = new ArrayList<>();
	        }

	        int length = sqlArray.length;
	        TreeMap<Integer, String> map = new TreeMap<>();
	        TreeMap<Integer, List<String>> tablemap = new TreeMap<>();
	        Map<String, String> aliasmap = new HashMap<>();
	        for (int i = 0; i < length; i++) {
	            if ("from".equals(sqlArray[i])) {
	                checkTable(sqlArray, i + 1, ignoreTables, map, tablemap, aliasmap);
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
	                list.add(i, SqlHelper.createWheres(aliasmap, tablemap.get(i), params, nmap.get(i)));
	            } else {
	                list.add(SqlHelper.createWheres(aliasmap, tablemap.get(i), params, nmap.get(i)));
	            }
	        }

	        return SqlHelper.toSql(list);
	    }


	    private void checkTable(String[] sqlArray, int pos, List<String> ignoreTables, TreeMap<Integer, String> map, TreeMap<Integer, List<String>> tablemap, Map<String, String> aliasmap) {
	        List<String> list = new ArrayList<>();
	        StringBuilder sb = new StringBuilder();
	        boolean flag = true;
	        boolean where = false;
	        int position = 0;
	        for (int i = pos; i < sqlArray.length; i++) {
	            if ("where".equals(sqlArray[i])) {
	                flag = false;
	                where = true;
	                position = i + 1;
	                break;
	            }

	            if (("left".equals(sqlArray[i]) || "right".equals(sqlArray[i]) || "inner".equals(sqlArray[i]) || "full".equals(sqlArray[i])) && "join".equals(sqlArray[i + 1])) {
	                flag = false;
	            }

	            if (")".equals(sqlArray[i])) {
	                flag = false;
	                position = i;
	                break;
	            }
	            if (flag) {
	                sb.append(sqlArray[i]).append(" ");
	            }
	        }

	        if (flag) {
	            position = sqlArray.length;
	        }

	        String[] tables = StringUtils.split(sb.toString().trim(), ",");

	        for (String t : tables) {
	            if (StringUtils.contains(t.trim(), " ")) {
	                String table = StringUtils.split(t.trim(), " ")[0];
	                if (!ignoreTables.contains(table)) {
	                    String alias = StringUtils.split(t.trim(), " ")[1];
	                    list.add(table);
	                    aliasmap.put(table, alias);
	                }
	            } else {
	                if (!ignoreTables.contains(t)) {
	                    list.add(t);
	                }
	            }
	        }

	        if (list.size() > 0) {
	            map.put(position, where ? "Y" : "N");
	            tablemap.put(position, list);
	        }

	    }

}
