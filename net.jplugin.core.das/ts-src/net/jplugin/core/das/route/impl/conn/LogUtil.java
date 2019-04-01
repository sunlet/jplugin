package net.jplugin.core.das.route.impl.conn;

import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;

public class LogUtil extends RefAnnotationSupport{
	public static LogUtil instance = new LogUtil();
	
	@RefLogger
	Logger logger;
	public void log(SqlHandleResult shr) {
		if (logger.isDebugEnabled()){
			logger.debug("RouteSql:"+shr.getResultSql());
			logger.debug("RouteDataSource:"+shr.getTargetDataSourceName());
		}
	}

}
