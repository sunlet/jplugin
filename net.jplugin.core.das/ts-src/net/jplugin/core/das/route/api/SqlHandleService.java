package net.jplugin.core.das.route.api;

import java.util.List;

import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.sqlhandler.SqlHandleServiceImpl;

public interface SqlHandleService {
	public static SqlHandleService INSTANCE = new SqlHandleServiceImpl();

	SqlHandleResult handle(RouterConnection conn, String sql, List<Object> params);

	SqlHandleResult handle(RouterConnection conn, String sql);
}
