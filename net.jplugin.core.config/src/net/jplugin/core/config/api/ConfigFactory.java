package net.jplugin.core.config.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.jplugin.common.kits.StringKit;
/**
*
* @author: LiuHang
* @version 创建时间：2015-10-12 下午01:07:22
**/
public class ConfigFactory {

	private static IConfigProvidor localConfigProvidor;
	private static IConfigProvidor remoteConfigProvidor=null;

	public static String getStringConfig(String path,String def){
		String val = _getStringConfig(path);
		if (StringKit.isNull(val)) return def;
		else return val;
	}
	public static String getStringConfig(String path){
		return getStringConfig(path,null);
	}
	public static String getStringConfigWithTrim(String path){
		String v = getStringConfig(path,null);
		if (v!=null) 
			v = v.trim();
		return v;
	}
	public static Long getLongConfig(String path,Long def){
		String val = _getStringConfig(path);
		if (StringKit.isNull(val)) return def;
		else return Long.parseLong(val);
	}
	
	public static Long getLongConfig(String path){
		return getLongConfig(path,null);
	}

	
	public static Integer getIntConfig(String path,Integer def){
		String val = _getStringConfig(path);
		if (StringKit.isNull(val)) return def;
		return Integer.parseInt(val);
	}

	public static Integer getIntConfig(String path){
		return getIntConfig(path,null);
	}
	
	public static Set<String> getGroups(){
		Set<String> ret=new HashSet();
		
		if (remoteConfigProvidor!=null) {
			Set<String> temp = remoteConfigProvidor.getGroups();		
			ret.addAll(temp);
		}
		
		ret.addAll(localConfigProvidor.getGroups());
		return ret;
	}

	/**
	 * 本地优先
	 * @param group
	 * @return
	 */
	public static Map<String,String> getStringConfigInGroup(String group){
		Map<String,String> ret=null;
		if (remoteConfigProvidor!=null) 
			ret = remoteConfigProvidor.getStringConfigInGroup(group);		
		
		if (ret==null) 
			ret = new HashMap<String,String>();
		
		ret.putAll(localConfigProvidor.getStringConfigInGroup(group));
		return ret;
	}
	/**
	 * <pre>
	 * 如果本地配置了该key，则返回本地
	 * 否则如果远程初始化好了，则返回远程，否则返回null。
	 * 注意：由于初始化顺序原因，访问该方法的时候，提放了远程有没有初始化好的情况！
	 * </pre>
	 * @param path
	 * @return
	 */
	private static String _getStringConfig(String path){
		if (localConfigProvidor.containsConfig(path))
			return localConfigProvidor.getConfigValue(path);
		
		if (remoteConfigProvidor!=null) 
			return remoteConfigProvidor.getConfigValue(path);
		
		return null;
	}

	public static void _setLocalConfigProvidor(IConfigProvidor repo) {
		localConfigProvidor = repo;
	}
	
	public static void _setRemoteConfigProvidor(IConfigProvidor repo) {
		remoteConfigProvidor = repo;
		System.out.println("$$$ Remote Configigure Providor init:"+repo.getClass().getName());
	}
}
