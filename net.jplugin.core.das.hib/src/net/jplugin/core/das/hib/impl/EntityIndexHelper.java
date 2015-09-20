package net.jplugin.core.das.hib.impl;


import java.sql.Connection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.hib.api.Entity;
import net.jplugin.core.das.hib.api.IDataService;
import net.jplugin.core.das.hib.api.IPersistObjDefinition;
import net.jplugin.core.das.hib.api.SinglePoDefine;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-24 下午07:07:50
 **/

public class EntityIndexHelper {

	private static final int MAX_INDEX_LEN = 64;

	/**
	 * @param conn 
	 * @param podefs
	 * @param singlePoDefs 
	 */
	public static void initIndexes(Connection conn, IPersistObjDefinition[] podefs, SinglePoDefine[] singlePoDefs) {
		for (IPersistObjDefinition pod:podefs){
			Class[] clzs = pod.getClasses();
			for (Class c:clzs){
				initIndexes(conn,c);
			}
		}
		for (SinglePoDefine s:singlePoDefs){
			initIndexes(conn,s.getPoClass());
		}
	}

	/**
	 * @param c
	 */
	private static void initIndexes(Connection conn,Class c) {
		Entity e = (Entity) c.getAnnotation(Entity.class);
		if (e == null){
			return;
		}
		
		if (e.indexes()==null || e.indexes().length==0){
			return;
		}
		
		String tablename = StringKit.isNull(e.tableName())? ReflactKit.getShortName(c):e.tableName();
		
		//获取小写的属性集
		Set<String> properties = ReflactKit.getProperties(c);
		Set<String> lownercasePropert = new HashSet<String>();
		for (String p:properties){
			lownercasePropert.add(p.trim().toLowerCase());
		}
		
		for (String indexStr:e.indexes()){
			if (StringKit.isNull(indexStr)) continue;
			validate(lownercasePropert,indexStr);
			createIndex( conn,indexStr,tablename);
		}
	}

	/**
	 * @param indexStr
	 * @param nameSet
	 * @param tbname 
	 * @return
	 */
	private static String createIndex(Connection conn,String indexStr, String tbname) {
		IDataService das = ServiceFactory.getService(IDataService.class);
		String idxname = getIndexName(tbname,indexStr);
		String sql = "CREATE INDEX "+idxname+" ON "+tbname+" ("+indexStr+")";
		try{
			SQLTemplate.executeCreateSql(conn, sql);
		}catch(Exception e){
//			e.printStackTrace();
			System.out.println("SQL ERROR:"+sql);
		}
		return idxname;
	}

	/**
	 * @param indexStr
	 * @param nameSet
	 * @return
	 */
	private static String getIndexName(String tbname,String indexStr) {
		String ret = "idx_" +tbname.toLowerCase().trim()+"_" +indexStr.toLowerCase().trim();
		ret = ret.replace(',', '_');
		ret = StringKit.replaceStr(ret, " ", "");
		//mysql 索引名称长度限制为64，够用了，先不控制索引长度
		
		if (ret.length() > MAX_INDEX_LEN){
			throw new RuntimeException("name too long:"+ret);
		}
		
		return ret;
		
		
//		if (ret.length()>MAX_INDEX_LEN){
//			ret = ret.substring(0,MAX_INDEX_LEN - 3);
//		}
//		for (int i=1;i<99;i++){
//			String indexRet = ret +"_"+i;
//			if (!nameSet.contains(indexRet)){
//				return indexRet;
//			}
//		}
//		throw new RuntimeException("Can't generate the index name:"+tbname+" "+indexStr);
	}



	/**
	 * @param s 
	 * @param indexStr
	 */
	private static void validate(Set<String> s, String indexStr) {
		indexStr = indexStr.toLowerCase();
		String[] parr = StringKit.splitStr(indexStr,",");
		for (String p:parr){
			if (!s.contains(p.toLowerCase().trim())){
				throw new RuntimeException("Index str error,field not exist:"+indexStr);
			}
		}
	}

}
