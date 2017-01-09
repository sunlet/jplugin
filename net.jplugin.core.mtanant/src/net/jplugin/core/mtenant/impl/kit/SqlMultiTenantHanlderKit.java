package net.jplugin.core.mtenant.impl.kit;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class SqlMultiTenantHanlderKit {
	/**
	 * <pre>
	 * #多租户配置列表
	 * #表示是否启用多租户模式，默认FALSE
	 * mtenant.enable=FALSE|TRUE
	 * 
	 * #指定区分多租户字段的名称.如果mtenant.enable=TRUE,则这项必须配置
	 * mtenant.field=field1
	 * 
	 * #启用多租户的数据源，默认ALL。可选配置。
	 * mtenant.datasource=ALL|ds1,ds2,ds3
	 * 
	 * #指定各个数据源中不支持多租户的表（例外表）。可选配置
	 * mtenant.datasource.ds1.exclude=table1,table2
	 * mtenant.datasource.ds2.exclude=table1,table2
	 * 
	 * #说明一下
	 * #如果碰到忽略多租户处理的注释，则忽略该SQL的处理：\/* IGNORE-TANANT *\/
	 * </pre>
	 * @param dataSourceName
	 * @param sql
	 * @return
	 */

	public static String handle(String dataSourceName, String sql) {
		ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		return sql;
	}

}
