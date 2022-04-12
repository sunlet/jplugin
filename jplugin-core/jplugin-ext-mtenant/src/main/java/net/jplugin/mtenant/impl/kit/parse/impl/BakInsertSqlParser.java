package net.jplugin.mtenant.impl.kit.parse.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.jplugin.mtenant.impl.kit.parse.SqlParser;
import net.jplugin.mtenant.impl.kit.util.SqlHelper;
import net.jplugin.mtenant.impl.kit.util.StringUtils;

/**
 * @author gaojh
 * @date 2017/1/7
 */
public class BakInsertSqlParser implements SqlParser {
    @Override
    public String parse(String sourceSql, Map<String, Object> params, List<String> ignoreTables) {

        if (params.isEmpty()) {
            return sourceSql;
        }


        String sql = SqlHelper.format(sourceSql);
        if (!StringUtils.startsWith(sql, "insert")) {
            return sql;
        }

        String[] sqlArray = StringUtils.split(sql, " ");
        if (ignoreTables == null) {
            ignoreTables = new ArrayList<>();
        }

        String tableName = sqlArray[2];
        if (ignoreTables.contains(tableName)) {
            return sourceSql;
        }

        int col;
        int val = -1;
        if (StringUtils.equals("(", sqlArray[3])) {
            col = 4;

            for (int i = 0; i < sqlArray.length; i++) {
                if ("values".equals(sqlArray[i])) {
                    val = i + 2;
                }
            }

            StringBuilder cols = new StringBuilder();
            StringBuilder vals = new StringBuilder();
            for (String key : params.keySet()) {
                cols.append(key).append(",");
                Object value = params.get(key);
                if (value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof BigDecimal) {
                    vals.append(value).append(",");
                } else {
                    vals.append("'").append(value).append("',");
                }
            }


            List<String> list = new ArrayList<>();
            list.addAll(Arrays.asList(sqlArray));

            if (val + 1 <= list.size()) {
                list.add(val, vals.toString());
            } else {
                list.add(vals.toString());
            }

            
            list.add(col, cols.toString());
            
            
            return SqlHelper.toSql(list);
        } else {
            return sourceSql;
        }


    }

}
