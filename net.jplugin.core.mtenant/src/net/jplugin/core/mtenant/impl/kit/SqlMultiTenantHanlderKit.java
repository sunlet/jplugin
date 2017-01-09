package net.jplugin.core.mtenant.impl.kit;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class SqlMultiTenantHanlderKit {
	/**
	 * @param dataSourceName
	 * @param sql
	 * @return
	 */

	public static String handle(String dataSourceName, String sql) {
		ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		return sql;
	}

}
