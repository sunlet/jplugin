package net.jplugin.core.config.api;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.impl.ConfigRepository;
/**
*
* @author: LiuHang
* @version ����ʱ�䣺2015-10-12 ����01:07:22
**/
public class ConfigFactory {

	private static ConfigRepository repository;

	public static String getStringConfig(String path,String def){
		String val = repository.getConfig(path);
		if (StringKit.isNull(val)) return def;
		else return val;
	}
	public static String getStringConfig(String path){
		return getStringConfig(path,null);
	}
	public static Long getLongConfig(String path,Long def){
		String val = repository.getConfig(path);
		if (StringKit.isNull(val)) return def;
		else return Long.parseLong(val);
	}
	
	public static Long getLongConfig(String path){
		return getLongConfig(path,null);
	}

	
	public static Integer getIntConfig(String path,Integer def){
		String val = repository.getConfig(path);
		if (StringKit.isNull(val)) return def;
		return Integer.parseInt(val);
	}

	public static Integer getIntConfig(String path){
		return getIntConfig(path,null);
	}


	public static void setRepository(ConfigRepository repo) {
		repository = repo;
	}
	

}
