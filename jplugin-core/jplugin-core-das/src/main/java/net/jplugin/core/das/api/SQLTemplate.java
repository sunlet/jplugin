package net.jplugin.core.das.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.ObjectRef;
import net.jplugin.common.kits.PritiveKits;
import net.jplugin.common.kits.PritiveKits.Transformer;
import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.common.kits.tuple.Tuple3;
import net.jplugin.common.kits.tuple.Tuple4;
import net.jplugin.core.das.api.SQLTemplate.BeansResultDisposer;
import net.jplugin.core.das.api.SqlTransormerManager.ISqlColumnFetcher;
import net.jplugin.core.das.api.SqlTransormerManager.SqlDateTransformer;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-2-24 上午09:02:50
 **/

public class SQLTemplate {

	public static boolean printSQL=true;
	
	public static <T> List<T> selectForBeans(Connection conn,String sql,Class<T> t,Object[] p){
		BeansResultDisposer<T> rd = new BeansResultDisposer<T>(t);
		executeSelect(conn,sql,rd,p);
		return rd.getList();
	}
	
	public static <T> List<T> selectWithMapForBeans(Connection conn,String sql,Class<T> t,Map<String,Object> map){
		BeansResultDisposer<T> rd = new BeansResultDisposer<T>(t);
		executeSelectWithMap(conn,sql,rd,map);
		return rd.getList();
	}
	
//	public static <T> List<T> selectWithBeanForBeans(Connection conn,String sql,Class<T> t,Object conditionBean){
//		BeansResultDisposer<T> rd = new BeansResultDisposer<T>(t);
//		executeSelectWithMap(conn,sql,rd,map);
//		return rd.getList();
//	}
	
	public List<Map<String, String>> select(Connection conn,String sql,Object[] p) {
		return executeSelect(conn, sql, p);
	}
	
	public int insert(Connection conn, String sql,Object[] param) {
		return executeInsertSql(conn, sql, param);
	}
	
	public List<Long> insertAndReturnGenKey(Connection conn, String sql,Object[] param) {
		return executeInsertReturnGenKey(conn, sql, param);
	}
	public int update(Connection conn, String sql,Object[] param) {
		return executeUpdateSql(conn, sql, param);
	}
	
	public int delete(Connection conn, String sql,Object[] param) {
		return executeDeleteSql(conn, sql, param);
	}
	
	/**
	 * @param conn
	 * @param sql
	 * @param param
	 * @return
	 */
	public static int executeUpdateSql(Connection conn, String sql,
			Object[] param) {
		if (printSQL){
			print(sql,param);
		}
		
		return executeAndReturnCount(conn,sql,param,"UPDATE");
	}
	


	/**
	 * @param connection
	 * @param sql
	 * @param param
	 * @return
	 */
	public static int executeDeleteSql(Connection conn, String sql,
			Object[] param) {
		if (printSQL){
			print(sql,param);
		}
		return executeAndReturnCount(conn,sql,param,"DELETE");
	}

	
	static int executeAndReturnCount(Connection conn, String sql,
			Object[] param,String prefixIgnorecase){
		String leftTenChar = sql.substring(0, 10);
		if (!leftTenChar.trim().toUpperCase().startsWith(prefixIgnorecase)) {
			throw new DataException("Not a valid sql with "+prefixIgnorecase);
		}
		Statement stmt = null;
		try {
			if (param == null || param.length == 0) {
				stmt = conn.createStatement();
				return stmt.executeUpdate(sql);
			} else {
				stmt = conn.prepareStatement(sql);
				for (int i = 0; i < param.length; i++) {
					((PreparedStatement) stmt).setObject(i + 1, param[i]);
				}
				int ret = ((PreparedStatement) stmt).executeUpdate();
				return ret;
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage()+"SQL执行失败。 SQL="+sql, e);
		}finally{
			closeStmtQuiretly(stmt);
		}
	}

	/**
	 * @param stmt
	 */
	private static void closeStmtQuiretly(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param connection
	 * @param sql
	 * @return
	 */
	public static void executeDropSql(Connection connection, String sql) {
		if (printSQL){
			print(sql,null);
		}
		executeAndReturnCount(connection,sql,null,"DROP");
	}
	/**
	 * @param connection
	 * @param sql
	 * @return
	 */
	public static void executeCreateSql(Connection connection, String sql) {
		if (printSQL){
			print(sql,null);
		}
		executeAndReturnCount(connection,sql,null,"CREATE");
	}
	
	public static List<Long> executeInsertReturnGenKey(Connection conn,String sql,Object[] param) {
		if (!sql.trim().toUpperCase().startsWith("INSERT")) {
			throw new DataException("Not a insert sql");
		}
		Statement stmt = null;
		try {
			if (param == null || param.length == 0) {
				stmt = conn.createStatement();
				stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
				return getGenKey(stmt);
			} else {
				stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < param.length; i++) {
					((PreparedStatement) stmt).setObject(i + 1, param[i]);
				}
				int ret = ((PreparedStatement) stmt).executeUpdate();
				return getGenKey(stmt);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage()+"SQL执行失败。 SQL="+sql, e);
		}finally{
			closeStmtQuiretly(stmt);
		}
	}
	

	private static List<Long> getGenKey(Statement stmt) throws SQLException {
		ResultSet genkeys = stmt.getGeneratedKeys();
		List<Long> ret = new ArrayList<Long>();
		while(genkeys.next()) {
			long key = genkeys.getLong(1);
			ret.add(key);
		}
		return ret;
	}

	/**
	 * @param connection
	 * @param sql
	 * @param param
	 * @return
	 */
	public static int executeInsertSql(Connection connection, String sql,
			Object[] param) {
		if (printSQL){
			print(sql,param);
		}
		return executeAndReturnCount(connection,sql,param,"INSERT");
	}

	public static List<Map<String,String>> executeSelect(Connection conn,String sql,Object[] p){
		return executeSelectWithMeta(conn,sql,p,false).first;
	}
	

	
	public static Tuple3<List<Map<String,String>>,List<String>,List<Integer>> executeSelectWithMeta(Connection conn,String sql,Object[] p,boolean needMeta){
		List<Map<String,String>> ret = new ArrayList<>();

		final ObjectRef<List<String>> columnsRef = new ObjectRef<>();
		final ObjectRef<List<Integer>> typesRef =  new ObjectRef<>();
		
		executeSelect(conn, sql,new IResultDisposer() {
			List<String> columns=null; //总是获取，不一定返回
			List<Integer> types=null;//不一定获取
			@Override
			public void readRow(ResultSet rs) throws SQLException {
//				rs.getMetaData().getColumnCount();
				if (columns==null){
					initcolumns(rs);
				}
				Map<String,String>  map = new HashMap<>();
				for (String s:columns){
					map.put(s, rs.getString(s));
				}
				ret.add(map);
			}
			private void initcolumns(ResultSet rs) throws SQLException {
				ResultSetMetaData m = rs.getMetaData();
				int cnt = m.getColumnCount();
				columns = new ArrayList<String>();
				for (int i=1;i<=cnt;i++){
					columns.add(m.getColumnLabel(i));
				}
				columnsRef.set(columns);
				
				if (needMeta){
					types = new ArrayList<Integer>();
					for (int i=1;i<=cnt;i++){
						types.add(m.getColumnType(i));
					}
					typesRef.set(types);
				}
			}
		},p);
		if (needMeta)
			return Tuple3.with(ret, columnsRef.get(), typesRef.get());
		else
			return Tuple3.with(ret, null, null);
	}
	
	/**
	 * 执行select，带着map格式的参数。
	 * 1. sql语句当中支持#{XXX}，替换为？ ，绑定的值为根据XXX从map获取的值。
	 * 2. sql语句中支持[]，表示可选的内容。如果里面的 #XXX 从map当中获取的结果为null，则对应的sql片段被去除
	 * 
	 * @param conn
	 * @param sql
	 * @param rd
	 * @param param
	 */
	public static void executeSelectWithMap(Connection conn,String sql, IResultDisposer rd,
			Map<String,Object> param) {
		Tuple2<String, Object[]> result = SqlTemplateHelperForMap.filterSqlWithMap(sql,param,true);
		executeSelect(conn,result.first,rd,result.second);
	}
	/**
	 * @param sql
	 * @param rd
	 * @param param
	 * @return
	 */
	public static void executeSelect(Connection conn,String sql, IResultDisposer rd,
			Object[] param) {
		if (printSQL){
			print(sql,param);
		}
		
		String leftTenChar = sql.substring(0, 10);
		if (!leftTenChar.trim().toUpperCase().startsWith("SELECT")) {
			throw new DataException("Not a valid sql with SELECT");
		}
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (param == null || param.length == 0) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				fetchRs(rs,rd);
			} else {
				stmt = conn.prepareStatement(sql);
				for (int i = 0; i < param.length; i++) {
					((PreparedStatement) stmt).setObject(i + 1, param[i]);
				}
				rs = ((PreparedStatement) stmt).executeQuery();
				fetchRs(rs,rd);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage()+" SQL="+sql, e);
		}finally{
			closeResultSetQuiertly(rs);
			closeStmtQuiretly(stmt);
		}
	}

	/**
	 * @param rs
	 */
	private static void closeResultSetQuiertly(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param rs
	 * @param rd
	 * @throws SQLException 
	 */
	private static void fetchRs(ResultSet rs, IResultDisposer rd) throws SQLException {
		while(rs.next()){
			rd.readRow(rs);
		}
	}
	
	private static void print(String sql, Object[] param) {
		System.out.print("SQL:"+sql+" params="+JsonKit.object2Json(param));
		System.out.println();
	}


	
	//这个类保存字段 和 Bean属性的相关映射信息
	static class ColAndBeanPropertyMapping{
		String colName;//字段名。bean属性名也是它，可能不完全，大小写不匹配。
		int colJDBCType;//字段类型
		Class beanPropertyType;//Bean属性字段
		Method beanSetterMethod;//Bean Setter方法
	}
	
	static class BeansResultDisposer <T> implements IResultDisposer {
		private Class<T> clazz;
		List<T> list = new LinkedList<T>();
		ColAndBeanPropertyMapping[] mappingArr;
		
		public BeansResultDisposer(Class<T> t) {
			this.clazz = t;
		}

		@Override
		public void readRow(ResultSet rs) throws SQLException {
			if (mappingArr==null) 
				init(rs,this.clazz);
			
			Object obj;
			try {
				obj = clazz.newInstance();
				list.add((T) obj);
			} catch (Exception e) {
				throw new DataException(e.getMessage(),e);
			}
			
			//设置object的值
			for (int i=0;i<mappingArr.length;i++) {
				Object value;
				ColAndBeanPropertyMapping mapping = mappingArr[i];
				
				ISqlColumnFetcher trans = SqlTransormerManager.getSqlFetcher(mapping.colJDBCType);
				if (trans==null) {
					value = rs.getObject(i+1,mapping.beanPropertyType);
				}else {
					value = trans.getDataForType(rs,i+1, mapping.beanPropertyType);
				}
				
				try {
					mapping.beanSetterMethod.invoke(obj, value);
				} catch (Exception e) {
					throw new DataException(e.getMessage(),e);
				}
			}
		}
		
		
		private void init(ResultSet rs ,Class beanClazz) throws SQLException {
			ResultSetMetaData md = rs.getMetaData();
			int cnt = md.getColumnCount();
			
			mappingArr = new ColAndBeanPropertyMapping[cnt];
			
			Map<String, Class<?>> beanPropertymap = ReflactKit.getPropertiesAndType(clazz);
			
			for (int i=0;i<cnt;i++) {
				ColAndBeanPropertyMapping item = new ColAndBeanPropertyMapping();
				mappingArr[i]=item;
				
				item.colName = md.getColumnLabel(i+1).toLowerCase();
				item.colJDBCType= md.getColumnType(i+1);
				
				//我们要求每一个数据库字段都要映射到一个bean属性，但是bean属性并不一定映射到数据库字段
				boolean foundProperty=false;
				for (Entry<String, Class<?>> en:beanPropertymap.entrySet()) {
					String beanProperty = en.getKey();
					if (beanProperty.equalsIgnoreCase(item.colName)){
						item.beanPropertyType = en.getValue();
						
						String method="set"+beanProperty.substring(0,1).toUpperCase()+beanProperty.substring(1);
						item.beanSetterMethod = ReflactKit.findSingeMethodExactly(beanClazz, method);
						
						foundProperty=true;
						break;
					}
				}
				if (!foundProperty) {
					throw new DataException("Can't find a property for column : "+item.colName);
				}
			}
		}

		public List<T> getList() {
			return list;
		}
	}

}
