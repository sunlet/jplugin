package net.jplugin.core.das.route.impl.sqlhandler;

import java.util.List;

import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.parser.SqlWordsWalker;

public abstract class AbstractCommandHandler {

	public abstract SqlHandleResult handle(RouterConnection conn, String sql, List<Object> params, SqlWordsWalker walker) ;

	protected TableConfig getTableCfg(RouterConnection conn,String tableName) {
		TableConfig tableCfg = conn.getDataSource().getConfig().findTableConfig(tableName);
		if (tableCfg ==null) new TablesplitException("The table is configed as a splate table."+tableName);
		return tableCfg;
	}

}
