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
 * @date 2017/1/7
 * 鍒嗗甫where鍜屼笉甯here涓ょ
 * 濡傛灉涓嶅甫where锛岀洿鎺ュ姞涓妛here
 * 濡傛灉甯here锛屽鐞嗗畠
 * 鏈�濂藉啀澶勭悊涓�涓嬪瓙鏌ヨ
 */
public class UpdateSqlParser implements SqlParser {
    @Override
    public String parse(String sourceSql, Map<String, Object> params, List<String> ignoreTables) {

        if (params.isEmpty()) {
            return sourceSql;
        }


        String sql = SqlHelper.format(sourceSql);
        if (!StringUtils.startsWith(sql, "update")) {
            return sql;
        }

        String[] sqlArray = StringUtils.split(sql, " ");
        if (ignoreTables == null) {
            ignoreTables = new ArrayList<>();
        }

        if (!StringUtils.contains(sql, "where") && !ignoreTables.contains(sqlArray[1])) {
            return sql + " " + SqlHelper.createWhere(params, "N");
        }

        int length = sqlArray.length;
        TreeMap<Integer, String> map = new TreeMap<>();
        for (int i = 0; i < length; i++) {
            if ("update".equals(sqlArray[i])) {
                String tableName = sqlArray[i + 1];
                if (ignoreTables.contains(tableName)) {
                    continue;
                }

                int wherePos = findFirst(sqlArray, "where");
                map.put(wherePos + 1, "Y");
            }

            if ("from".equals(sqlArray[i])) {
                String tableName = sqlArray[i + 1];
                if (ignoreTables.contains(tableName)) {
                    continue;
                }
                if (i + 2 == length) {
                    map.put(i + 2, "N");
                } else if ("where".equals(sqlArray[i + 2])) {
                    map.put(i + 3, "Y");
                } else {
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


    private int findFirst(String[] array, String item) {
        for (int i = 0; i < array.length; i++) {
            if (item.equals(array[i])) {
                return i;
            }
        }

        return 0;
    }
}
