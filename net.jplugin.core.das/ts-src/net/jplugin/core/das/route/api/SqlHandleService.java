package net.jplugin.core.das.route.api;

import java.util.List;

import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.sqlhandler.SqlHandleServiceImpl;
import net.jplugin.core.das.route.impl.sqlhandler2.SqlHandleServiceImpl2;

public interface SqlHandleService {
	public static SqlHandleService INSTANCE = new SqlHandleServiceImpl();
	public static SqlHandleService INSTANCE4SPAN = new SqlHandleServiceImpl2();

	SqlHandleResult handle(RouterConnection conn, String sql, List<Object> params);

	SqlHandleResult handle(RouterConnection conn, String sql);
}
