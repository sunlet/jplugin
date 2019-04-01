package net.jplugin.core.das.sqlrefactor;

import java.sql.Connection;

import net.jplugin.core.das.api.sqlrefactor.ISqlRefactor;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class SqlRefactorManager {
	static ISqlRefactor[] refactors ;
	public static void init(){
		refactors = PluginEnvirement.INSTANCE.getExtensionObjects(net.jplugin.core.das.Plugin.EP_SQL_REFACTOR,ISqlRefactor.class);
	}
	
	public static boolean hasSqlRefactor(){
		return refactors.length>0;
	}

	/**
	 * 早注册的先处理，晚注册的后处理
	 * @param dataSourceName
	 * @param sql
	 * @param inner
	 * @return
	 */
	public static String handleSql(String dataSourceName, String sql, Connection inner) {
		String sqlTemp = sql;
		for (ISqlRefactor o:refactors){
			sqlTemp = o.refactSql(dataSourceName, sqlTemp,inner);
		}
		return sqlTemp;
	}

}
