package net.jplugin.core.das.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.jplugin.common.kits.JsonKit;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-2-24 上午09:02:50
 **/

public class SQLTemplate {

	public static boolean printSQL=true;
	/**
	 * @param conn
	 * @param sql
	 * @param param
	 * @return
	 */
	public static int executeUpdateSql(Connection conn, String sql,
			Object[] param) {
		if (printSQL){
			print(sql,param);
		}
		
		return executeAndReturnCount(conn,sql,param,"UPDATE");
	}
	
	private static void print(String sql, Object[] param) {
		System.out.print("SQL:"+sql+" params="+JsonKit.object2Json(param));
		System.out.println();
	}


	/**
	 * @param connection
	 * @param sql
	 * @param param
	 * @return
	 */
	public static int executeDeleteSql(Connection conn, String sql,
			Object[] param) {
		if (printSQL){
			print(sql,param);
		}
		return executeAndReturnCount(conn,sql,param,"DELETE");
	}

	
	static int executeAndReturnCount(Connection conn, String sql,
			Object[] param,String prefixIgnorecase){
		String leftTenChar = sql.substring(0, 10);
		if (!leftTenChar.trim().toUpperCase().startsWith(prefixIgnorecase)) {
			throw new DataException("Not a valid sql with "+prefixIgnorecase);
		}
		Statement stmt = null;
		try {
			if (param == null || param.length == 0) {
				stmt = conn.createStatement();
				return stmt.executeUpdate(sql);
			} else {
				stmt = conn.prepareStatement(sql);
				for (int i = 0; i < param.length; i++) {
					((PreparedStatement) stmt).setObject(i + 1, param[i]);
				}
				int ret = ((PreparedStatement) stmt).executeUpdate();
				return ret;
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage()+"SQL执行失败。 SQL="+sql, e);
		}finally{
			closeStmtQuiretly(stmt);
		}
	}

	/**
	 * @param stmt
	 */
	private static void closeStmtQuiretly(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param connection
	 * @param sql
	 * @return
	 */
	public static void executeDropSql(Connection connection, String sql) {
		if (printSQL){
			print(sql,null);
		}
		executeAndReturnCount(connection,sql,null,"DROP");
	}
	/**
	 * @param connection
	 * @param sql
	 * @return
	 */
	public static void executeCreateSql(Connection connection, String sql) {
		if (printSQL){
			print(sql,null);
		}
		executeAndReturnCount(connection,sql,null,"CREATE");
	}

	/**
	 * @param connection
	 * @param sql
	 * @param param
	 * @return
	 */
	public static int executeInsertSql(Connection connection, String sql,
			Object[] param) {
		if (printSQL){
			print(sql,param);
		}
		return executeAndReturnCount(connection,sql,param,"INSERT");
	}

	/**
	 * @param sql
	 * @param rd
	 * @param param
	 * @return
	 */
	public static void executeSelect(Connection conn,String sql, IResultDisposer rd,
			Object[] param) {
		if (printSQL){
			print(sql,param);
		}
		
		String leftTenChar = sql.substring(0, 10);
		if (!leftTenChar.trim().toUpperCase().startsWith("SELECT")) {
			throw new DataException("Not a valid sql with SELECT");
		}
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (param == null || param.length == 0) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				fetchRs(rs,rd);
			} else {
				stmt = conn.prepareStatement(sql);
				for (int i = 0; i < param.length; i++) {
					((PreparedStatement) stmt).setObject(i + 1, param[i]);
				}
				rs = ((PreparedStatement) stmt).executeQuery();
				fetchRs(rs,rd);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage()+" SQL="+sql, e);
		}finally{
			closeResultSetQuiertly(rs);
			closeStmtQuiretly(stmt);
		}
	}

	/**
	 * @param rs
	 */
	private static void closeResultSetQuiertly(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param rs
	 * @param rd
	 * @throws SQLException 
	 */
	private static void fetchRs(ResultSet rs, IResultDisposer rd) throws SQLException {
		while(rs.next()){
			rd.readRow(rs);
		}
	}


}
