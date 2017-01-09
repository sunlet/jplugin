package net.jplugin.core.mtanant.impl.kit;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class SqlMultiTanantHanlderKit {
	/**
	 * <pre>
	 * #多租户配置列表
	 * #表示是否启用多租户模式，默认FALSE
	 * mtanant.enable=FALSE|TRUE
	 * 
	 * #指定区分多租户字段的名称.如果mtanant.enable=TRUE,则这项必须配置
	 * mtanant.field=field1
	 * 
	 * #启用多租户的数据源，默认ALL。可选配置。
	 * mtanant.datasource=ALL|ds1,ds2,ds3
	 * 
	 * #指定各个数据源中不支持多租户的表（例外表）。可选配置
	 * mtanant.datasource.ds1.exclude=table1,table2
	 * mtanant.datasource.ds2.exclude=table1,table2
	 * 
	 * </pre>
	 * @param dataSourceName
	 * @param sql
	 * @return
	 */

	public static String handle(String dataSourceName, String sql) {
		return sql+"/*aaaaaaa*/";
	}

}
