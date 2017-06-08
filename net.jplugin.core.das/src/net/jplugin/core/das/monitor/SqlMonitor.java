package net.jplugin.core.das.monitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.jplugin.core.das.api.SqlMonitorListenerContext;

public class SqlMonitor {

	public static Object execute(StatemenWrapper stmt, String methodName, String sql, SqlCall sc) throws SQLException {
		SqlMonitorListenerContext ctx = stmt.getCtx();

		SqlMonitorListenerManager.instance.beforeExecute(ctx);
		try {
			Object ret = sc.call();
			if (ret instanceof ResultSet) {
				return new ResultSetWrapper(stmt.getDataSourceName(), (ResultSet) ret, sql);
			} else
				return ret;
		} catch (Exception e) {
			return setAndRethrow(ctx, e);
		} finally {
			SqlMonitorListenerManager.instance.afterExecute(ctx);
		}
	}

	public static Object createStatement(ConnectionWrapper conn, SqlCall sc) throws SQLException {
		Object ret = sc.call();
		return new StatemenWrapper(conn.getDataSourceName(), (Statement) ret, null);
	}

	public static Object prepareStatement(ConnectionWrapper conn, String sql, SqlCall sc) throws SQLException {
		Object ret = sc.call();
		return new StatemenWrapper(conn.getDataSourceName(), (Statement) ret, sql);
	}

	public static Object next(ResultSetWrapper rs, SqlCall sc) throws SQLException {
		SqlMonitorListenerContext ctx = rs.getCtx();
		
		// 如果before被阻挡，after就不要执行了。所以存在有before但是没有after的情况！！
		SqlMonitorListenerManager.instance.beforeNext(ctx);
		try {
			return sc.call();
		} catch (Exception e) {
			return setAndRethrow(ctx, e);
		} finally {
			SqlMonitorListenerManager.instance.afterNext(ctx);
		}
	}

	// private static Object runWithListener(SqlCall sc,
	// SqlMonitorListenerContext ctx) throws SQLException {
	// // 如果before被阻挡，after就不要执行了。所以存在有before但是没有after的情况！！
	// SqlMonitorListenerManager.instance.beforeExecute(ctx);
	// try {
	// return sc.call();
	// } catch (Exception e) {
	// return setAndRethrow(ctx, e);
	// } finally {
	// SqlMonitorListenerManager.instance.afterExecute(ctx);
	// }
	// }

	private static Object setAndRethrow(SqlMonitorListenerContext c, Exception e) throws SQLException {
		c.setException(e);
		if (e instanceof RuntimeException)
			throw (RuntimeException) e;
		if (e instanceof SQLException)
			throw (SQLException) e;
		throw new RuntimeException(e);
	}
}
