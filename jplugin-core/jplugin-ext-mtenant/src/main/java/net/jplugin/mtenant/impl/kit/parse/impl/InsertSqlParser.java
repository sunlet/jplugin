package net.jplugin.mtenant.impl.kit.parse.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.jplugin.mtenant.impl.kit.parse.SqlParser;
import net.jplugin.mtenant.impl.kit.util.SqlHelper;
import net.jplugin.mtenant.impl.kit.util.StringUtils;

/**
 * @author gaojh
 * @date 2017/1/7
 */
public class InsertSqlParser implements SqlParser {
	@Override
	public String parse(String sourceSql, Map<String, Object> params,
			List<String> ignoreTables) {

		if (params.isEmpty()) {
			return sourceSql;
		}

		String sql = SqlHelper.format(sourceSql);
		if (!StringUtils.startsWith(sql, "insert")) {
			return sql;
		}
		// 对含嵌套的不处理
		if (sourceSql.contains("select")) {
			throw new IllegalArgumentException("SQL parse error");
		}

		String[] sqlArray = StringUtils.split(sql, " ");
		if (ignoreTables == null) {
			ignoreTables = new ArrayList<>();
		}

		//tableName  取值有误
		String tableName = sqlArray[2];
		if (ignoreTables.contains(tableName)) {
			return sourceSql;
		}

		int col = 0;
		int paramKeyPosition = 0;
		int length = sqlArray.length;
		Stack<String> subSqlStack = new Stack<String>();
		String valuesString = "";

		String resultSql = "";
		for (int i = 0; i < length; i++) {
			if("into".equals(sqlArray[i])){
				paramKeyPosition = i+3;
			}
			// 必须放在values前，此时resultSql含有了values字段
			resultSql = resultSql + " " + sqlArray[i];
			if ("values".equals(sqlArray[i])) {
				col = i + 1;
				break;
			}
		}
		for (int i = col; i < length; i++) {
			if (subSqlStack.isEmpty()) {
				// 压栈
				subSqlStack.push(sqlArray[i]);
			}else if(SqlHelper.checkFunctionStr(sqlArray[i])){
				//还原sql内置函数，函数可能带参
				String sqlFunction = sqlArray[i]+sqlArray[++i];
				String leftBracket = sqlArray[++i];
				while (i < length)
				{
					sqlFunction = sqlFunction + leftBracket;
					if(")".equals(leftBracket)){
						break;
					}else {
						leftBracket = sqlArray[++i];
					}	
				}
				subSqlStack.push(sqlFunction);
			}else if (sqlArray[i].equals(")")) {
				// 出栈
				int stackSize = subSqlStack.size();
				String rightBracket = ")";
				String subPartSql = "";
				for (int j = 0; j < stackSize; j++) {
					if (subSqlStack.peek().equals("(")) {
						subPartSql = subSqlStack.pop() + " "
								+ getParaValue(params) + ", " + subPartSql
								+ rightBracket;
						break;
					} else {
						subPartSql = subSqlStack.pop() + " " + subPartSql;
					}
				}
				valuesString = valuesString + "," + subPartSql;

			} else {

				subSqlStack.push(sqlArray[i]);
			}
		}
		// 去掉一个逗号
		resultSql = resultSql + valuesString.substring(1);

		String[] resultSqlArray = StringUtils.split(resultSql, " ");
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList(resultSqlArray));
		list.add(paramKeyPosition,getParaKey(params)+",");
		
		return SqlHelper.toSql(list);

	}
	private String getParaKey(Map<String, Object> params){
		String toReturn = "";
		for (String key : params.keySet()) {
			toReturn = key;
		}
		return toReturn;
	}
	private String getParaValue(Map<String, Object> params) {
		String toReturn = "";
		for (String key : params.keySet()) {
			Object value = params.get(key);
			if (value instanceof Integer || value instanceof Long
					|| value instanceof Double || value instanceof BigDecimal) {
				toReturn = value.toString();
			} else {
				toReturn = "'" + value + "'";
			}
		}
		return toReturn;
	}

}
