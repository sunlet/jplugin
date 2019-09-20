package net.jplugin.core.das.route.api;

import java.util.List;

import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;

public interface ITsAutoCreation {
	boolean needCreate(TableConfig tbCfg,String dataSourceName,String tableName);
	
}
