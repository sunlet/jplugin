package net.jplugin.core.das.route.impl.algms;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.ITsAlgorithm.ValueType;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterException;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class TimeBasedSpanUtil {
	
//	public interface LocalDateMaintain{
//		public LocalDateTime maintain(LocalDateTime ld, int units);
//	}
	
	public static DataSourceInfo[] getResults(ITsAlgorithm algm, RouterDataSource dataSource, String tableName,
			ValueType valueType,RouterKeyFilter kva, ChronoUnit unit, int trackDays) {
		if (kva.getOperator()==Operator.ALL) 
			return TimeBasedSpanUtil.get(algm, dataSource, tableName, trackDays,unit);
		else if (kva.getOperator()==Operator.BETWEEN)
			return TimeBasedSpanUtil.getForBetween(algm, dataSource, tableName, valueType,kva.getConstValue() ,unit,trackDays);
		else if (kva.getOperator()==Operator.IN)
			return TimeBasedSpanUtil.getForIn(algm, dataSource, tableName,valueType, kva.getConstValue() ,unit);
		else 
			throw new RouterException("got error kva operator:"+kva.getOperator());
	}
	
	private static DataSourceInfo[] getForIn(ITsAlgorithm algm, RouterDataSource dataSource, String tableName,
			ValueType valueType, Object[] value, ChronoUnit unit) {
		LocalDateTime[] arr = new LocalDateTime[value.length];
		for(int i=0;i<arr.length;i++){
			arr[i] = TimeConverterKit.convetToLoalDate(valueType,value[i]);
		}
		
		return getFromTimeList(algm, dataSource, tableName, arr);
	}
	private static DataSourceInfo[] getForBetween(ITsAlgorithm algm, RouterDataSource dataSource, String tableName,
			ValueType valueType, Object[] value, ChronoUnit unit, int trackDays) {
		//处理between的单边界null值
		Object leftValue = value[0];
		Object rightValue = value[1];
		
		if (leftValue==null && rightValue == null){
			throw new RouterException("range can't be null.");
		}
		
		LocalDateTime left ,right;
		//赋值，要处理单边为空的条件
		if (leftValue==null){
			right = TimeConverterKit.convetToLoalDate(valueType,rightValue);
			left = minusUnits(right,unit,trackDays);//dm.maintain(right, trackDays);
		}else if (rightValue==null){
			left = TimeConverterKit.convetToLoalDate(valueType,leftValue);
			right = minusUnits(left,unit, -trackDays);
		}else{
			left = TimeConverterKit.convetToLoalDate(valueType,leftValue);
			right = TimeConverterKit.convetToLoalDate(valueType,rightValue);
		}
		//要trunc一下
		left = truncate(left,unit);
		right = truncate(right,unit);
		
		List<LocalDateTime> list = new ArrayList();
		long leftEpoch = CalenderKit.convertLocalDateTime2Date(left).getTime();
		for (int i=0;i<10000;i++){
			LocalDateTime newDate = minusUnits(right,unit,i);
			if (CalenderKit.convertLocalDateTime2Date(newDate).getTime()>=leftEpoch){
				list.add(newDate);
			}
		}
		//转换类型
		LocalDateTime[] arr = new LocalDateTime[list.size()];
		list.toArray(arr);
		
		return getFromTimeList(algm, dataSource, tableName, arr);
	}
	
	private static LocalDateTime truncate(LocalDateTime ld, ChronoUnit unit) {
		if (unit==ChronoUnit.DAYS)
			return ld.truncatedTo(ChronoUnit.DAYS);
		if (unit==ChronoUnit.MONTHS)
			return LocalDateTime.of(ld.getYear(), ld.getMonth(),1,0,0);
		if (unit==ChronoUnit.YEARS)
			return LocalDateTime.of(ld.getYear(),1,1,0,0);
		throw new RuntimeException("unsupport unit."+unit);
	}

	private static LocalDateTime minusUnits(LocalDateTime ld, ChronoUnit unit, int trackDays) {
		return ld.minus(trackDays,unit);
//		return dm.maintain(right, trackDays);
	}


	private static DataSourceInfo[] get(ITsAlgorithm algm,RouterDataSource dataSource, String tableName,int historyNum,ChronoUnit unit){
		LocalDateTime[] arr = new LocalDateTime[historyNum];
		LocalDateTime date = LocalDateTime.now();
		for (int i=0;i<historyNum;i++){
			LocalDateTime newDate = minusUnits(date,unit,i);//dm.maintain(date,i);
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

//		//check exists  and maintain
//		List<String> toRemove = new ArrayList();
//		List<String> toRemoveInternal = new ArrayList();
//		
//		for (Entry<String, Set<String>> en:mapList.entrySet()){
//			String dsName = en.getKey();
//			Set<String> value = en.getValue();
//			
//			//进行内层维护
//			toRemoveInternal.clear();
//			for (String v:value){
//				if (PluginEnvirement.INSTANCE.isUnitTesting() && !TableExistsCacheManager.exists(dsName, v)){
////					value.remove(v);
//					toRemoveInternal.add(v);
//				}
//			}
//			value.removeAll(toRemoveInternal);
//			
//			//插入外层待删除队列
//			if (value.size()==0) 
//				toRemove.add(dsName);
//		}
//		
//		//外层删除
//		for (String tb:toRemove){
//			mapList.remove(tb);
//		}
		
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
