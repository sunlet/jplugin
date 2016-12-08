package net.jplugin.core.das.route.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.api.TablesplitException;

public class TableAutoCreation {

	static ConcurrentHashMap<String, Integer> tableMapping=new ConcurrentHashMap<>();
	
	public static void tryCreate(TableConfig tc, Result result, String tbBaseName) {
		String key = result.getDataSource()+"#"+result.getTableName();
		//这里没有做同步控制，因为createtable的时间可能比较长。
		String sql = tc.getCreationSql();

		//有sql并且没有判断过该表才处理
		if (StringKit.isNotNull(sql) && !tableMapping.containsKey(key)){
			sql = StringKit.repaceFirstIgnoreCase(sql, tbBaseName, result.getTableName());
			tryCreateTable(result.getDataSource(),result.getTableName(),sql);
			//这里可能重复被放入
			tableMapping.put(key, 1);
		}
	}
	
	private static void tryCreateTable(String dataSource, String tableName, String sql) {
		DataSource ds = DataSourceFactory.getDataSource(dataSource);
		try{
			Connection conn = ds.getConnection();
			try{
				//select 成功执行不再创建
				SQLTemplate.executeSelect(conn, "select * from "+tableName +" where 1=2", new IResultDisposer() {
					@Override
					public void readRow(ResultSet rs) throws SQLException {
					}
				}, null);
			}catch(Exception e1){
				//select失败，才创建
				SQLTemplate.executeCreateSql(conn , sql);
			}
		}catch(Exception e){
			throw new TablesplitException(e);
		}
	}
}
