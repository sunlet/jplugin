package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.HashMap;
import java.util.List;

import net.jplugin.core.das.route.api.SqlHandleService;
import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;

public class SqlHandleServiceImpl2 implements SqlHandleService {

	HashMap<String, AbstractCommandHandler2> map = null;
	
	@Override
	public SqlHandleResult handle(RouterConnection conn, String sql, List<Object> params) {
		sql = sql.trim();
		
		AbstractCommandHandler2 handler = AbstractCommandHandler2.create(conn,sql,params);
		return handler.handle();
	}

	@Override
	public SqlHandleResult handle(RouterConnection conn, String sql) {
		return handle(conn,sql,null);
	}

}
