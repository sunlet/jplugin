package net.jplugin.core.das.route.impl.algms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.ITsAlgorithm.ValueType;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterException;
import net.jplugin.core.das.route.api.TablesplitException;

public class TimeBasedSpanUtil {
	
	public interface LocalDateMaintain{
		public LocalDateTime maintain(LocalDateTime ld, int units);
	}
	
	public static DataSourceInfo[] getResults(ITsAlgorithm algm, RouterDataSource dataSource, String tableName,
			ValueType valueType,RouterKeyFilter kva, LocalDateMaintain timeMaintainer, int trackDays) {
		if (kva.getOperator()==Operator.ALL) 
			return TimeBasedSpanUtil.get(algm, dataSource, tableName, trackDays,timeMaintainer);
		else if (kva.getOperator()==Operator.BETWEEN)
			return TimeBasedSpanUtil.getForBetween(algm, dataSource, tableName, valueType,kva.getConstValue() ,timeMaintainer,trackDays);
		else if (kva.getOperator()==Operator.IN)
			return TimeBasedSpanUtil.getForIn(algm, dataSource, tableName,valueType, kva.getConstValue() ,timeMaintainer);
		else 
			throw new RouterException("got error kva operator:"+kva.getOperator());
	}
	
	private static DataSourceInfo[] getForIn(ITsAlgorithm algm, RouterDataSource dataSource, String tableName,
			ValueType valueType, Object[] value, LocalDateMaintain dm) {
		LocalDateTime[] arr = new LocalDateTime[value.length];
		for(int i=0;i<arr.length;i++){
			arr[i] = convetToLoalDate(valueType,value[0]);
		}
		
		return getFromTimeList(algm, dataSource, tableName, arr);
	}
	private static DataSourceInfo[] getForBetween(ITsAlgorithm algm, RouterDataSource dataSource, String tableName,
			ValueType valueType, Object[] value, LocalDateMaintain dm, int trackDays) {
		//处理between的单边界null值
		Object leftValue = value[0];
		Object rightValue = value[1];
		
		if (leftValue==null && rightValue == null){
			throw new RouterException("range can't be null.");
		}
		
		LocalDateTime left ,right;
		//赋值，要处理单边为空的条件
		if (leftValue==null){
			right = convetToLoalDate(valueType,rightValue);
			left = dm.maintain(right, trackDays);
		}else if (rightValue==null){
			left = convetToLoalDate(valueType,leftValue);
			right = dm.maintain(left, -trackDays);
		}else{
			left = convetToLoalDate(valueType,leftValue);
			right = convetToLoalDate(valueType,rightValue);
		}
		
		List<LocalDateTime> list = new ArrayList();
		long leftEpoch = CalenderKit.convertLocalDateTime2Date(left).getTime();
		for (int i=0;i<10000;i++){
			LocalDateTime newDate = dm.maintain(right,i);
			if (CalenderKit.convertLocalDateTime2Date(newDate).getTime()>=leftEpoch){
				list.add(newDate);
			}
		}
		//转换类型
		LocalDateTime[] arr = new LocalDateTime[list.size()];
		list.toArray(arr);
		
		return getFromTimeList(algm, dataSource, tableName, arr);
	}
	
	private static LocalDateTime convetToLoalDate(ValueType vt,Object object) {
		switch(vt){
		case DATE:
			return CalenderKit.convertDate2LocalDateTime((Date) object);
		case TIMESTAMP:
			return CalenderKit.convertDate2LocalDateTime((Date) object);
		case LONG:
			return CalenderKit.convertDate2LocalDateTime(new Date((Long)object));
		}
		throw new RouterException("unsupported type:"+object.getClass());
	}

	private static DataSourceInfo[] get(ITsAlgorithm algm,RouterDataSource dataSource, String tableName,int historyNum,LocalDateMaintain dm){
		LocalDateTime[] arr = new LocalDateTime[historyNum];
		LocalDateTime date = LocalDateTime.now();
		for (int i=0;i<historyNum;i++){
			LocalDateTime newDate = dm.maintain(date,i);
			arr[i] = newDate;
		}
		return getFromTimeList(algm, dataSource, tableName, arr);
	}

	private static DataSourceInfo[] getFromTimeList(ITsAlgorithm algm, RouterDataSource dataSource, String tableName,
			LocalDateTime[] arr) {
		Map<String,Set<String>> mapList = new HashMap<>();
		for (int i=0;i<arr.length;i++){
			LocalDateTime newDate = arr[i];
			Result r = algm.getResult(dataSource,tableName,ValueType.TIMESTAMP, Timestamp.valueOf(newDate));
			//get or create list
			
			Set<String> list = mapList.get(r.getDataSource());
			if (list==null){
				list = new HashSet<>();
				mapList.put(r.getDataSource(), list);
			}
			
			//add it
			list.add(r.getTableName());
		}

		//check exists  and maintain
		List<String> toRemove = new ArrayList();
		List<String> toRemoveInternal = new ArrayList();
		
		for (Entry<String, Set<String>> en:mapList.entrySet()){
			String dsName = en.getKey();
			Set<String> value = en.getValue();
			
			//进行内层维护
			toRemoveInternal.clear();
			for (String v:value){
				if (PluginEnvirement.INSTANCE.isUnitTesting() && !TableExistsCacheManager.exists(dsName, v)){
//					value.remove(v);
					toRemoveInternal.add(v);
				}
			}
			value.removeAll(toRemoveInternal);
			
			//插入外层待删除队列
			if (value.size()==0) 
				toRemove.add(dsName);
		}
		
		//外层删除
		for (String tb:toRemove){
			mapList.remove(tb);
		}
		
		//make ret arr
		DataSourceInfo[] ret = new DataSourceInfo[mapList.size()];
		int index = 0;
		for (Entry<String, Set<String>> en:mapList.entrySet()){
			DataSourceInfo dsi =new DataSourceInfo();
			dsi.setDsName(en.getKey());
			dsi.setDestTbs(en.getValue().toArray(new String[en.getValue().size()]));
			ret[index++] = dsi;
		}
		
		//return 
		return ret;
	}
	
//	public static DataSourceInfo[] get(ITsAlgorithm algm,RouterDataSource dataSource, String tableName,int historyNum,LocalDateMaintain dm){
//		LocalDate date = LocalDate.now();
//		Map<String,List<String>> mapList = new HashMap<>();
//		for (int i=0;i<historyNum;i++){
//			LocalDate newDate = dm.maintain(date,i);
//			Result r = algm.getResult(dataSource,tableName,ValueType.DATE, CalenderKit.convertToSqlDate(newDate));
//			
//			//get or create list
//			List<String> list = mapList.get(r.getDataSource());
//			if (list==null){
//				list = new ArrayList<>();
//				mapList.put(r.getDataSource(), list);
//			}
//			
//			//add it
//			list.add(r.getTableName());
//		}
//		
//		//check exists  and maintain
//		List<String> toRemove = new ArrayList();
//		for (Entry<String, List<String>> en:mapList.entrySet()){
//			String dsName = en.getKey();
//			List<String> value = en.getValue();
//			for (int i=value.size()-1;i>=0;i--){
//				if (!TimeBasedSpanUtil.exists(dsName, value.get(i))){
//					value.remove(i);
//				}
//			}
//			if (value.size()==0) 
//				toRemove.add(dsName);
//		}
//		for (String tb:toRemove){
//			mapList.remove(tb);
//		}
//		
//		//make ret arr
//		DataSourceInfo[] ret = new DataSourceInfo[mapList.size()];
//		int index = 0;
//		for (Entry<String, List<String>> en:mapList.entrySet()){
//			DataSourceInfo dsi =new DataSourceInfo();
//			dsi.setDsName(en.getKey());
//			dsi.setDestTbs(en.getValue().toArray(new String[en.getValue().size()]));
//			ret[index++] = dsi;
//		}
//		
//		//return 
//		return ret;
//	}
	
	



}
