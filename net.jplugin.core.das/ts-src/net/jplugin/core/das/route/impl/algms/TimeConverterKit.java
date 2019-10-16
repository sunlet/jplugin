package net.jplugin.core.das.route.impl.algms;

//import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Date;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.core.das.route.api.RouterException;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.api.ITsAlgorithm.ValueType;

public class TimeConverterKit {

	public static long convertToTimeLong(ValueType vt, Object key) {
//		long time;
		switch (vt) {
		case DATE:
			java.sql.Date dt = (java.sql.Date) key;
			return dt.getTime();
		case TIMESTAMP:
			return ((java.sql.Timestamp) key).getTime();
		case STRING:
			return convertStringTimeToDate((String) key).getTime();
		case LONG:
			return (long) key;
		default:
			throw new TablesplitException("DateAlgm don't support type:" + vt);
		}
		
			
//		if (vt == ValueType.DATE){
//			java.sql.Date dt = (java.sql.Date) key;
//			time = dt.getTime();
//		}else if (vt == ValueType.TIMESTAMP){
//			time = ((java.sql.Timestamp)key).getTime();
//		}else if (vt == ValueType.STRING) {
//			time = convertStringTimeToDate((String)key).getTime();
//		}else if (vt == ValueType.LONG){ 
//			time = (long)key;
//		}else
//			throw new TablesplitException("DateAlgm don't support type:"+vt);
		
//		return time;
	}
	
	private static Date convertStringTimeToDate(String key) {
		String pattern;
		switch(key.length()){
		case 4:
			//yyyy
			pattern = "yyyy";
			break;
		case 6:
			//yyyyMM
			pattern = "yyyyMM";
			break;
		case 8:
			//yyyyMMdd
			pattern = "yyyyMMdd";
			break;
		case 14:
			//yyyyMMddHHmmss
			pattern = "yyyyMMddHHmmss";
			break;
		default:
			throw new RuntimeException("unsupport string time format:"+key);
		}
		return CalenderKit.getTimeFromString(key, pattern);
	}

	public static LocalDateTime convetToLoalDate(ValueType vt,Object object) {
		switch(vt){
		case DATE:
			return CalenderKit.convertDate2LocalDateTime((java.util.Date) object);
		case TIMESTAMP:
			return CalenderKit.convertDate2LocalDateTime((java.util.Date) object);
		case STRING:
			return CalenderKit.convertDate2LocalDateTime(convertStringTimeToDate((String)object));
		case LONG:
			return CalenderKit.convertDate2LocalDateTime(new java.util.Date((Long)object));
		}
		throw new RouterException("unsupported type:"+object.getClass());
	}

}
