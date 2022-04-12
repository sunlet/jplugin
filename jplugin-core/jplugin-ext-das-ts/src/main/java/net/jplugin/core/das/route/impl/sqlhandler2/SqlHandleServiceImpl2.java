package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.HashMap;
import java.util.List;

import net.jplugin.core.das.dds.api.IRouterDataSource;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.SqlHandleService;
//import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;

public class SqlHandleServiceImpl2 extends RefAnnotationSupport implements SqlHandleService {

	HashMap<String, AbstractCommandHandler2> map = null;
	
	@RefLogger
	Logger logger;
	@Override
	public SqlHandleResult handle(RouterDataSource dataSource, String sql, List<Object> params) {
		sql = sql.trim();
		
		AbstractCommandHandler2 handler = AbstractCommandHandler2.create(dataSource,sql,params);
		SqlHandleResult result = handler.handle();
		
		if (logger.isInfoEnabled()){
			logger.info("TableSplitHandle Result:"+result);
		}
		
		return result;
	}

	@Override
	public SqlHandleResult handle(RouterDataSource dataSource, String sql) {
		return handle(dataSource,sql,null);
	}

}
