package net.jplugin.mtenant.impl.kit.parse.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import net.jplugin.mtenant.impl.kit.parse.SqlParser;
import net.jplugin.mtenant.impl.kit.util.SqlHelper;
import net.jplugin.mtenant.impl.kit.util.StringUtils;

public class SelectSqlParser implements SqlParser {

	@Override
	public String parse(String sourceSql, Map<String, Object> params,
			List<String> ignoreTables) {
		if (params.isEmpty()) {
			return sourceSql;
		}
		if (!sourceSql.contains("from")) {
			return sourceSql;
		}
		String returnSql =  SqlHelper.format(sourceSql);
		if (returnSql.contains("union all")) {
			String preSql = SqlHelper.format(returnSql.substring(0,returnSql.indexOf("union all")));
			String subSql = SqlHelper.format(returnSql.substring(returnSql.indexOf("union all")).replace("union all", ""));
			returnSql = parse(preSql, params, ignoreTables) + " union all "
					+ parse(subSql, params, ignoreTables);

		} else {
			if (!returnSql.contains("(")) {
				returnSql = preParse(returnSql, params, ignoreTables);
			} else {
				returnSql = dealBracket(returnSql, params, ignoreTables);
			}
		}

		boolean havePlatform = false;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (returnSql.contains(entry.getKey())) {
				havePlatform = true;
				break;
			}
		}
		boolean haveIgnore = false;
		if (!havePlatform) {
			for (String ignore : ignoreTables) {
				if (returnSql.contains(ignore)) {
					haveIgnore = true;
					break;
				}
			}
		}
		// 锟斤拷锟斤拷from锟街凤拷锟斤拷sql锟斤拷洌拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟絧latform_num
		if (!haveIgnore && !havePlatform && !returnSql.contains("from")) {
			throw new IllegalArgumentException("SQL parse error");
		} else {
			return returnSql;
		}

	}

	public String preParse(String sourceSql, Map<String, Object> params,
			List<String> ignoreTables) {
		if (params.isEmpty()) {
			return sourceSql;
		}
		if (!sourceSql.contains("select")) {
			return sourceSql;
		}

		String[] sqlArray = StringUtils.split(sourceSql, " ");
		if (ignoreTables == null) {
			ignoreTables = new ArrayList<>();
		}
		boolean whereIf = false;
		int length = sqlArray.length;
		TreeMap<Integer, String> map = new TreeMap<>();
		Set<String> aliasList = new HashSet<String>();
		int fromPosition = 0;
		int wherePosition = 0;
		int beforeByPosition = length;
		for (int i = 0; i < length; i++) {
			if ("from".equals(sqlArray[i])) {
				fromPosition = i;
				if (i + 1 == length) {
					// select * from
					throw new RuntimeException("error sql: " + sourceSql);
				}

			}
			if ("limit".equals(sqlArray[i])
					|| (("order".equals(sqlArray[i]) || "group"
							.equals(sqlArray[i])) && "by"
							.equals(sqlArray[i + 1]))) {
				// 只锟斤拷beforByPosition取锟斤拷小值
				if (i < beforeByPosition) {
					beforeByPosition = i;
				}
			}
			if ("where".equals(sqlArray[i])) {
				wherePosition = i;
				map.put(i + 1, "Y");
				whereIf = true;

			}
		}
		StringBuilder sb = new StringBuilder();
		if (whereIf) {
			for (int i = fromPosition + 1; i < wherePosition; i++) {

				if (!"as".equals(sqlArray[i]) && !"on".equals(sqlArray[i])
						&& !sqlArray[i].contains(".")
						&& !sqlArray[i].equals("=")
						&& !sqlArray[i].equals("join")
						&& !sqlArray[i].equals("and")
						&& !sqlArray[i].equals("or")) {
					if (("left".equals(sqlArray[i])
							|| "right".equals(sqlArray[i])
							|| "inner".equals(sqlArray[i]) || "full"
								.equals(sqlArray[i]))
							&& "join".equals(sqlArray[i + 1])) {
						sb.append(",");
					} else {

						sb.append(sqlArray[i]).append(" ");
					}
				}

			}
		}
		// 锟斤拷示锟斤拷锟斤拷锟斤拷where
		if (wherePosition == 0) {
			for (int i = fromPosition + 1; i < beforeByPosition; i++) {
				if (!"as".equals(sqlArray[i]) && !"on".equals(sqlArray[i])
						&& !sqlArray[i].contains(".")
						&& !sqlArray[i].equals("=")
						&& !sqlArray[i].equals("join")
						&& !sqlArray[i].equals("and")
						&& !sqlArray[i].equals("or")) {
					if (("left".equals(sqlArray[i])
							|| "right".equals(sqlArray[i])
							|| "inner".equals(sqlArray[i]) || "full"
								.equals(sqlArray[i]))
							&& "join".equals(sqlArray[i + 1])) {
						sb.append(",");
					} else {

						sb.append(sqlArray[i]).append(" ");
					}
				}
			}

		}
		String[] tables = StringUtils.split(sb.toString().trim(), ",");
		for (String t : tables) {
			if (StringUtils.contains(t.trim(), " ")) {
				aliasList.add(StringUtils.split(t.trim(), " ")[1]);

			} else {
				aliasList.add(t.trim());
			}
		}

		if (0 < aliasList.size()) {
			Map<String, Object> bridgeMap = new HashMap<String, Object>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {

				for (String alias : aliasList) {
					if (!ignoreTables.contains(alias)
							&& !alias.startsWith("tempT_able_")) {
						bridgeMap.put(alias + "." + entry.getKey(),
								entry.getValue());
					}
				}
			}
			params = bridgeMap;
		}
		List<String> bracketList = new ArrayList<>();
		// 锟斤拷没锟斤拷where
		if (!whereIf) {
			map.put(beforeByPosition, "N");

		} else {
			bracketList.addAll(Arrays.asList(sqlArray));
			bracketList.add(wherePosition + 1, "(");
			// 锟斤拷为锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷(锟斤拷锟斤拷锟斤拷锟杰筹拷锟饺硷拷锟斤拷1
			bracketList.add(beforeByPosition + 1, ")");

		}
		if (map.size() == 0) {
			return sourceSql;
		}
		NavigableMap<Integer, String> nmap = map.descendingMap();
		List<String> list = new ArrayList<>();
		if (0 < bracketList.size()) {
			list.addAll(bracketList);
		} else {
			list.addAll(Arrays.asList(sqlArray));
		}
		for (int i : nmap.keySet()) {
			if (i + 1 <= list.size()) {
				list.add(i, SqlHelper.createWhere(params, nmap.get(i)));
			} else {
				list.add(SqlHelper.createWhere(params, nmap.get(i)));
			}
		}
		return SqlHelper.toSql(list);
	}

	public String dealTempTables(String multiSql,
			Map<String, String> tempTableMap) {
		for (Map.Entry<String, String> tempTable : tempTableMap.entrySet()) {
			multiSql = multiSql.replace(tempTable.getKey(),
					tempTable.getValue());
		}
		if (multiSql.contains("tempT_able_")) {
			multiSql = dealTempTables(multiSql, tempTableMap);
		}
		return multiSql;
	}

	public String dealBracket(String sourceSql, Map<String, Object> params,
			List<String> ignoreTables) {
		String[] sqlArray = StringUtils.split(sourceSql, " ");
		int length = sqlArray.length;
		// 锟斤拷锟斤拷锟斤拷嵌锟斤拷select
		Stack<String> subSqlStack = new Stack<String>();
		Map<String, String> tempTableMap = new HashMap<String, String>();
		for (int i = 0; i < length; i++) {

			if (subSqlStack.isEmpty()) {
				// 锟斤拷锟秸伙拷强盏锟�
				subSqlStack.push(sqlArray[i]);
			} else if (sqlArray[i].equals(")")) {
				// 说锟斤拷锟斤拷时栈锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟脚ｏ拷锟斤拷始锟斤拷栈
				int stackSize = subSqlStack.size();
				String rightBracket = ")";
				String subPartSql = "";
				for (int j = 0; j < stackSize; j++) {
					if (subSqlStack.peek().equals("(")) {
						subPartSql = subSqlStack.pop()
								+ preParse(subPartSql, params, ignoreTables)
								+ " " + rightBracket;
						break;
					} else {
						subPartSql = subSqlStack.pop() + " " + subPartSql;
					}
				}

				String tempTableKey = "tempT_able_" + i+"_T";
				tempTableMap.put(tempTableKey, subPartSql);
				subSqlStack.push(tempTableKey);

			} else {

				subSqlStack.push(sqlArray[i]);
			}
		}
		// 锟斤拷锟斤拷嵌锟斤拷锟斤拷锟斤拷锟揭伙拷锟斤拷锟斤拷宕︼拷锟�
		String wholeSql = "";
		int stackLength = subSqlStack.size();
		for (int j = 0; j < stackLength; j++) {
			wholeSql = subSqlStack.pop() + " " + wholeSql;
		}
		String multiSql = preParse(wholeSql, params, ignoreTables);

		multiSql = dealTempTables(multiSql, tempTableMap);

		return multiSql;
	}

}