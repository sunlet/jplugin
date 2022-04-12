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
import net.jplugin.core.das.dds.api.TablesplitException;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;

public class TableAutoCreation {

	static ConcurrentHashMap<String, Long> tableMapping=new ConcurrentHashMap<>();
	
	/**
	 * 为了测试时候需要清理
	 */
	public static void clearCache(){
		tableMapping.clear();
	}
//	
//	public static void tryCreate(TableConfig tc, DataSourceInfo[] result, String tbBaseName,ITsAlgorithm algm) {
//		//提早返回
//		if (StringKit.isNull(tc.getCreationSql()))
//			return;
//		
//		for (DataSourceInfo r:result){
//			for (String tb:r.getDestTbs()){
//				tryCreate(tc,r.getDsName(),tb,tbBaseName,algm);
//			}
//		}
//	}
//	
//	public static void tryCreate(TableConfig tc, String dataSource,String tableName, String tbBaseName, ITsAlgorithm algm) {
//		String key = dataSource+"#"+tableName;
//		//这里没有做同步控制，因为createtable的时间可能比较长。
//		String sql = tc.getCreationSql();
//
//		//有sql并且没有判断过该表才处理
//		if (StringKit.isNotNull(sql) && !tableMapping.containsKey(key)){
//			if (algm instanceof ITsAutoCreation){
//				//需要创建，才会在Mapping当中放内容，避免 可能会在map当中放大量垃圾
//				if (((ITsAutoCreation)algm).needCreate(tc, dataSource, tableName)){
//					
//					sql = StringKit.repaceFirstIgnoreCase(sql, tbBaseName, tableName);
//					doCheck(dataSource,tableName,sql);
//					//这里可能重复被放入
//					tableMapping.put(key, 1);
//				}
//			}
//		}
//	}
	
	public static boolean checkExists(TableConfig tc, String dataSource, String tableName) {
		return doCheck(dataSource,tableName,null,null,false);
	}
	
	public static boolean checkExistsAndCreate(TableConfig tc, String dataSource, String tableName, String tbBaseName) {
		String sql = tc.getCreationSql();

		if (StringKit.isNotNull(sql)) {
			return doCheck(dataSource,tableName,tbBaseName,sql,true);
		}else {
			//虽然传入参数想创建，但是如果不存在还是创建不了。此时应该是在执行INSERT sql语句失败的场景。
			return doCheck(dataSource,tableName,tbBaseName,sql,false);
		}
	}
	
	
	private static final Long EXISTS_FLAG=1L;
	private static final Long REPLAT_CHECK_NOTEXISTS_TABLE_INTERVAL = 1000 * 180L;
	/**
	 * 目前一个不存在的表每隔3分钟被验证，存在的表只会验证一次
	 * @param dataSource
	 * @param tableName
	 * @param sql
	 * @param createIfNotExist
	 * @return
	 */
	private synchronized static boolean doCheck(String dataSource, String tableName, String baseTbName,String sql,boolean createIfNotExist) {
		//检查是否已经验证过存在
		String key = dataSource + "#" + tableName;
		Long flag = tableMapping.get(key);
		if (EXISTS_FLAG.equals(flag)){
			//验证过存在
			return true;
		}else{
			if (createIfNotExist==false && flag!=null && System.currentTimeMillis()-flag <= REPLAT_CHECK_NOTEXISTS_TABLE_INTERVAL){
				//上次检查没超时，返回false
				return false;
			}else{
				//重复检查
			}
		}
		
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
				
				//检查存在，标记一下
				tableMapping.put(key, EXISTS_FLAG);
				return true;
			}catch(Exception e1){
				//检查失败
				if (createIfNotExist){
					//表不存在，则创建
					String finalSql = StringKit.repaceFirstIgnoreCase(sql, baseTbName, tableName);
					SQLTemplate.executeCreateSql(conn , finalSql);
					//成功创建以后，标记为存在
					tableMapping.put(key, EXISTS_FLAG);
					return true;
				}else{
					//表不存在,放入当前时间
					tableMapping.put(key, System.currentTimeMillis());
					return false;
				}
			}
		}catch(Exception e){
			throw new TablesplitException(e);
		}
	}

//	public static boolean tableExists(String ds, String tb) {
//		String key = ds + "#" + tb;
//		return tableMapping.containsKey(key);
//	}


}
