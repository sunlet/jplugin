package net.jplugin.core.das.route.impl.algms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.ITsAlgorithm.ValueType;

public class TimeBasedSpanUtil {
	
	public interface LocalDateMaintain{
		public LocalDate maintain(LocalDate ld, int units);
	}
	
	public static DataSourceInfo[] get(ITsAlgorithm algm,RouterDataSource dataSource, String tableName,int historyNum,LocalDateMaintain dm){
		LocalDate date = LocalDate.now();
		Map<String,List<String>> mapList = new HashMap<>();
		for (int i=0;i<historyNum;i++){
			LocalDate newDate = dm.maintain(date,i);
			Result r = algm.getResult(dataSource,tableName,ValueType.DATE, CalenderKit.convertToSqlDate(newDate));
			
			//get or create list
			List<String> list = mapList.get(r.getDataSource());
			if (list==null){
				list = new ArrayList<>();
				mapList.put(r.getDataSource(), list);
			}
			
			//add it
			list.add(r.getTableName());
		}
		
		//check exists  and maintain
		List<String> toRemove = new ArrayList();
		for (Entry<String, List<String>> en:mapList.entrySet()){
			String dsName = en.getKey();
			List<String> value = en.getValue();
			for (int i=value.size()-1;i>=0;i--){
				if (!TimeBasedSpanUtil.exists(dsName, value.get(i))){
					value.remove(i);
				}
			}
			if (value.size()==0) 
				toRemove.add(dsName);
		}
		for (String tb:toRemove){
			mapList.remove(tb);
		}
		
		//make ret arr
		DataSourceInfo[] ret = new DataSourceInfo[mapList.size()];
		int index = 0;
		for (Entry<String, List<String>> en:mapList.entrySet()){
			DataSourceInfo dsi =new DataSourceInfo();
			dsi.setDsName(en.getKey());
			dsi.setDestTbs(en.getValue().toArray(new String[en.getValue().size()]));
			ret[index++] = dsi;
		}
		
		//return 
		return ret;
	}
	
	private static boolean exists(String ds,String tableName){
		DataSource dataSource = DataSourceFactory.getDataSource(ds);
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
