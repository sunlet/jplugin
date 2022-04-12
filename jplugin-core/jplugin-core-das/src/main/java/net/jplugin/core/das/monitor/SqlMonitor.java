package net.jplugin.core.das.monitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.das.api.monitor.ISqlExecFilter;
import net.jplugin.core.das.api.monitor.ResultSetContext;
import net.jplugin.core.das.api.monitor.SqlMonitorListenerContext;
import net.jplugin.core.das.api.monitor.StatementContext;
import net.jplugin.core.kernel.api.PluginFilterManager;

public class SqlMonitor {

	static PluginFilterManager<Tuple2<StatementContext, SqlCall>> execFilter = new PluginFilterManager<>(
			net.jplugin.core.das.Plugin.EP_SQL_EXEC_FILTER, (fc, ctx) -> {
				return ctx.second.call();
			});
	
    static boolean hasFilter;
	
	public static void initExecFilter(){
		execFilter.init();
		//1个是自己执行用的
		if (execFilter.chainSize()>1) 
			hasFilter = true;
		else 
			hasFilter = false;
	}
	
	public static Object execute(StatemenWrapper stmt, String methodName, SqlCall sc) throws SQLException {
		StatementContext ctx = stmt.getCtx();

		SqlMonitorListenerManager.instance.beforeExecute(ctx);
		try {
//			Object ret = sc.call();
			Object ret = execFilter.filter(Tuple2.with(ctx,sc));
			if (ret instanceof ResultSet) {
				return new ResultSetWrapper(stmt.getDataSourceName(), (ResultSet) ret, stmt.getTheSql());
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
		ResultSetContext ctx = rs.getCtx();
		
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
		
		if (e instanceof SQLException)
			throw (SQLException) e;
		if (e.getCause()!=null && e.getCause() instanceof SQLException)
			throw (SQLException)e.getCause();
		throw new SQLException(e);
		
	}

	public static ResultSet getResultSet(StatemenWrapper stmt,SqlCall sc) throws SQLException {
		ResultSet ret = (ResultSet) sc.call();
		if (ret==null)
			return null;
		else 
			return new ResultSetWrapper(stmt.getDataSourceName(),ret,stmt.getTheSql());
		
	}


}
