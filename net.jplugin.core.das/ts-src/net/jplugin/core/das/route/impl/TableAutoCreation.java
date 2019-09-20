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
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.ITsAutoCreation;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.api.TablesplitException;

public class TableAutoCreation {

	static ConcurrentHashMap<String, Integer> tableMapping=new ConcurrentHashMap<>();
	
	/**
	 * 为了测试时候需要清理
	 */
	public static void clearCache(){
		tableMapping.clear();
	}
	
	public static void tryCreate(TableConfig tc, DataSourceInfo[] result, String tbBaseName,ITsAlgorithm algm) {
		//提早返回
		if (StringKit.isNull(tc.getCreationSql()))
			return;
		
		for (DataSourceInfo r:result){
			for (String tb:r.getDestTbs()){
				tryCreate(tc,r.getDsName(),tb,tbBaseName,algm);
			}
		}
	}
	
	public static void tryCreate(TableConfig tc, String dataSource,String tableName, String tbBaseName, ITsAlgorithm algm) {
		String key = dataSource+"#"+tableName;
		//这里没有做同步控制，因为createtable的时间可能比较长。
		String sql = tc.getCreationSql();

		//有sql并且没有判断过该表才处理
		if (StringKit.isNotNull(sql) && !tableMapping.containsKey(key)){
			if (algm instanceof ITsAutoCreation){
				//需要创建，才会在Mapping当中放内容，避免 可能会在map当中放大量垃圾
				if (((ITsAutoCreation)algm).needCreate(tc, dataSource, tableName)){
					
					sql = StringKit.repaceFirstIgnoreCase(sql, tbBaseName, tableName);
					tryCreateTable(dataSource,tableName,sql);
					//这里可能重复被放入
					tableMapping.put(key, 1);
				}
			}
		}
	}
	
	private synchronized static void tryCreateTable(String dataSource, String tableName, String sql) {
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
