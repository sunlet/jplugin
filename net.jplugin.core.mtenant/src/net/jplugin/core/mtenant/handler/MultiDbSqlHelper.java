package net.jplugin.core.mtenant.handler;

import java.util.List;

import net.jplugin.core.das.route.impl.parser.SqlStrLexerToolNew;
import net.jplugin.core.das.route.impl.parser.SqlWordsWalker;

/**
 * <pre>
 * 规则： 
 * select规则： 
 * 	from、join 后的第一个单词。 
 * 	from、join 到 ORDER|WHERE|GROUP|UNION|LIMIT|) 之间的所有","之后的第一个单词。
 * 
 * update规则： 
 * 	update 之后的第一个 
 * 	select规则
 * 
 * insert规则： 
 * 	into 之后的第一个 
 * 	select规则
 * 
 * DELETE规则： 
 * 	就是select规则
 * </pre>
 * @author LiuHang
 *
 */
public class MultiDbSqlHelper {
	private static final String SELECT = "SELECT";
	private static final Object UPDATE = "UPDATE";
	private static final Object DELETE = "DELETE";
	private static final Object INSERT = "INSERT";

	public static String handle(String sql, String schema) {
		SqlWordsWalker walker = SqlWordsWalker.createFromSql(sql);
		String[] list = walker.getArray();
		String command = list[0].toUpperCase();

		if (SELECT.equals(command)) {
			handleSelect(list, schema);
			return toSql(list);
		} else if (UPDATE.equals(command)) {
			handleUpdate(list, schema);
			handleSelect(list, schema);
			return toSql(list);
		} else if (DELETE.equals(command)) {
			handleSelect(list, schema);
			return toSql(list);
		} else if (INSERT.equals(command)) {
			handleInsert(list, schema);
			handleSelect(list, schema);
			return toSql(list);
		} else
			throw new RuntimeException("Unsupported sql for MultiDbSqlHelper");

	}

	private static void handleInsert(String[] list, String schema) {
		for (int i = 0; i < list.length; i++) {
			if ("INTO".equalsIgnoreCase(list[i])) {
				handleTableName(list, i + 1, schema);
				return;
			}
		}
		throw new RuntimeException("Can't find INTO word in "+toSql(list));
	}

	private static void handleUpdate(String[] list, String schema) {
		for (int i = 0; i < list.length; i++) {
			if ("UPDATE".equalsIgnoreCase(list[i])) {
				handleTableName(list, i + 1, schema);
				return;
			}
		}
		throw new RuntimeException("Can't find UPDATE word in "+toSql(list));
	}

	private static String toSql(String[] list) {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (String s : list) {
			if (first) {
				sb.append(s);
				first = false;
			} else if (s.equals("(")){
				sb.append(s);
			} else
				sb.append(" ").append(s);
		}
		return sb.toString();
	}

	private static void handleSelect(String[] list, String schema) {
		for (int i = 0; i < list.length; i++) {
			String word = list[i];
			if ("FROM".equalsIgnoreCase(word) || "JOIN".equalsIgnoreCase(word)) {
				handleFromOrJoin(list, schema, i);
			}
		}

	}

	private static void handleFromOrJoin(String[] list, String schema, int startPos) {
		// 处理第一个单词
		handleTableName(list, startPos + 1, schema);

		// 处理到 ORDER|WHERE|GROUP|UNION|LIMIT|)之间的逗号后的第一个，忽略（）中的内容
		for (int i = startPos+1; i < list.length; i++) {
			String word = list[i];

			if (word.equals("(")) {
				// 忽略括号的内容
				int posEnd = getMatchingBranketPos(list, i);
				i = posEnd + 1;
				if (i>=list.length)
					return;
				word = list[i];
			}
			if ("ORDER".equalsIgnoreCase(word) || "WHERE".equalsIgnoreCase(word) || "GROUP".equalsIgnoreCase(word)
					|| "UNION".equalsIgnoreCase(word) || "LIMIT".equalsIgnoreCase(word) || ")".equalsIgnoreCase(word)  || "UNION".equalsIgnoreCase(word)) {
				return;
			}
			
			//碰到from和join也要返回，相关内容会归入下一次handleFromOrJoin
			if ("FROM".equalsIgnoreCase(word) || "JOIN".equalsIgnoreCase(word))
				return;

			if (",".equals(word)) {
				if (!"(".equals(list[i + 1]))
					handleTableName(list, i + 1, schema);
			}
		}
	}

	private static int getMatchingBranketPos(String[] list, int startPos) {
		int i = startPos + 1;
		int leftBracketNum = 1;

		while (i < list.length) {
			String word = list[i];
			if ("(".equals(word))
				leftBracketNum++;
			if (")".equals(word)) {
				leftBracketNum--;
				if (leftBracketNum == 0)
					break;
			}
			i++;
		}

		if (i < list.length)
			return i;
		else {
			throw new RuntimeException("Can't find matching [)]  from index " + startPos + ", sql = " + toSql(list));
		}
	}

	private static void handleTableName(String[] list, int i, String schema) {
		String word = list[i];
		if (word.startsWith("/*")) {
			// 如果这个位置是注释，则处理下一个
			handleTableName(list, i + 1, schema);
		} else {
			if (word.indexOf(".") >= 0)
				throw new RuntimeException("The table name to handle must not contain [.] ,but is " + word);
			list[i] = schema + "." + word;
		}
	}

	public static void main(String[] args) {
		doHandle("select * from table1", "sss");
		doHandle("select * from table1 where f1=1 and f2=2", "sss");
		doHandle("select * from table1,(select * from ttt) t2 where table1.a = t2.b", "sss");
		doHandle("select * from a,(select * from a)", "xxx");
		
		test();
	}

	private static void doHandle(String sql, String schema) {
		String ret = handle(sql, schema);
		System.out.println();
		System.out.println(sql + " ->");
		System.out.println(ret);
	}

	public static void test() {

		String sql = "select sr.userId ,st.userId from sso_role sr, sso_toke st where sr.userid=123 and "
				+ "sr.role_id in (select role_id from sso_role where app_id in (select app_id from sso_app where app_id=1))";

		doHandle(sql, "ssssss");

		sql = "select distinct s.* from sso_role r, sso_role_resource_mapping m,sso_resource s where r.role_id = m.role_id and s.resource_id= m.resource_id and r.status =1 and s.status !=2 and s.type = 1 and r.role_id in ( ? , ? , ? , ? ) order by s.name ";

		doHandle(sql, "ssssss");
		sql = "SELECT count ( sr.role_id)   FROM   sso_user_role_mapping su,sso_role sr   WHERE   sr.role_id=su.role_id   AND right(sr.role_code,6)='_a8min'        AND su.user_id =?";
		doHandle(sql, "ssssss");

		sql = "select create_date as createdate, modify_date as modifydate, work_state as workstate, sso as sso, platform_num as platformnum, status as status from sso_user where status!=2 order by user_id asc limit 0, 10";

		doHandle(sql, "ssssss");
		sql = "select count(u.user_id  ) from sso_user u inner join sso_user_role_mapping m on u.user_id=m.user_id inner join sso_role r on r.role_id= m.role_id inner join sso_app a on a.app_id=r.app_id and r.role_code=?";
		doHandle(sql, "ssssss");

		sql = "SELECT sr1.app_id, sr1.role_id AS id, sr1.role_name AS NAME, CONCAT(sr1.app_id,sa1.app_code) AS pid, IF ( surm.user_id, 'true', 'false' ) AS checked,"
				+ "sr1.role_code as roleCode,sr1.platform_num sp,surm.platform_num sur,sa1.platform_num sap "
				+ "FROM sso_role sr1 ,sso_user_role_mapping surm , sso_app sa1 WHERE sr1.app_id = sa1.app_id AND sr1.`status` = 1 and sr1.role_id = surm.role_id AND surm.user_id =8";
		doHandle(sql, "ssssss");
		sql = "SELECT  count(1) FROM sso_role_resource_mapping m, sso_resource res,sso_app app WHERE m.resource_id = res.resource_id AND res.app_id = app.app_id "
				+ "AND m.role_id in (? ,?, ?, ? , ?, ?, ?, ?, ? , ? ) AND res.type = 4 AND app.app_code = ? AND res.url = ?";
		doHandle(sql, "ssssss");
	}
}
