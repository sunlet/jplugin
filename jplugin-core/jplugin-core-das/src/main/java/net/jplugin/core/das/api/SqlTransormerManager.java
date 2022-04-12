package net.jplugin.core.das.api;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.CalenderKit;

class SqlTransormerManager
{

	static interface ISqlColumnFetcher
	{
		<T> T  getDataForType(ResultSet rs, int colIndex, Class<T> beanPropertyType) throws SQLException;
	}
	
	static class SqlDateTransformer implements ISqlColumnFetcher{

		@Override
		public <T> T getDataForType(ResultSet rs, int colIndex, Class<T> target) throws SQLException {
			Date t = rs.getDate(colIndex);
			if (target.equals(java.util.Date.class)) {
				return (T) new java.util.Date(t.getTime());
			}
			if (target.equals(String.class)) {
				return (T) CalenderKit.getTimeString(t.getTime());
			}
			if (target.equals(java.sql.Date.class)) {
				return (T) t;
			}
			if (target.equals(Long.class) || target.equals(long.class)) {
				return (T)((Long) t.getTime());
			}
			
			throw new DataException("Can't transformer from "+t.getClass().getName()+" to "+target.getName());
		}
	}
	
	static class SqlTimeTransformer implements ISqlColumnFetcher{

		@Override
		public <T> T getDataForType(ResultSet rs, int colIndex, Class<T> target) throws SQLException {
			Time t = rs.getTime(colIndex);
			if (target.equals(java.util.Date.class)) {
				return (T) new java.util.Date(t.getTime());
			}
			if (target.equals(String.class)) {
				return (T) CalenderKit.getTimeString(t.getTime());
			}
			if (target.equals(java.sql.Date.class)) {
				return (T) t;
			}
			
			if (target.equals(Long.class) || target.equals(long.class)) {
				return (T)((Long) t.getTime());
			}
			throw new DataException("Can't transformer from "+t.getClass().getName()+" to "+target.getName());
		}
	}
	
	static class SqlTimeStampTransformer implements ISqlColumnFetcher{

		@Override
		public <T> T getDataForType(ResultSet rs, int colIndex, Class<T> target) throws SQLException {
			Timestamp t = rs.getTimestamp(colIndex);
			if (target.equals(java.util.Date.class)) {
				return (T) new java.util.Date(t.getTime());
			}
			if (target.equals(String.class)) {
				return (T) CalenderKit.getTimeString(t.getTime());
			}
			if (target.equals(java.sql.Date.class)) {
				return (T) t;
			}
			
			if (target.equals(Long.class) || target.equals(long.class)) {
				return (T)((Long) t.getTime());
			}
			throw new DataException("Can't transformer from "+t.getClass().getName()+" to "+target.getName());
		}
	}
	
	static Map<Integer,ISqlColumnFetcher> map = new HashMap<Integer, SqlTransormerManager.ISqlColumnFetcher>();
	static {
		ISqlColumnFetcher f = new SqlDateTransformer();
		map.put(Types.TIMESTAMP, f);
		map.put(Types.DATE, f);
		f = new SqlTimeTransformer();
		map.put(Types.TIME, f);
		f = new SqlTimeStampTransformer();
		map.put(Types.TIMESTAMP, f);
	}


	public static ISqlColumnFetcher getSqlFetcher(int colJDBCType) {
		return map.get(colJDBCType);
	}

}
