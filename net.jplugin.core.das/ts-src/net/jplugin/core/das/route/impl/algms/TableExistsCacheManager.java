package net.jplugin.core.das.route.impl.algms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.TablesplitException;

public class TableExistsCacheManager {

	static ConcurrentHashMap<String,Boolean> cache = new ConcurrentHashMap<>();
//	public static void evict(String ds,String tableName){
//		String key = ds+"#"+tableName;
//		cache.remove(key);
//	}
//	public static boolean exists(String ds,String tableName){
//		String key = ds+"#"+tableName;
//		Boolean v = cache.get(key);
//		if (v!=null){
//			v = existsInner(ds, tableName);
//			cache.put(key, v);
//		}
//		return v;
//	}

	public static boolean exists(String ds,String tableName){
		DataSource dataSource = DataSourceFactory.getDataSource(ds);
		if (dataSource instanceof RouterDataSource)
			throw new RuntimeException("Route conn can't call exists");
		Connection conn;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			throw new TablesplitException(e);
		}
		try{
			SQLTemplate.executeSelect(conn, "select * from "+tableName +" where 1=2", new IResultDisposer() {
				@Override
				public void readRow(ResultSet rs) throws SQLException {
				}
			}, null);
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
