package net.jplugin.core.kernel.api;

import java.util.Map;

import net.jplugin.common.kits.StringKit;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-16 上午11:12:50
 **/

public class ConfigHelper {
	private Map<String, String> conf;

	public ConfigHelper(Map<String, String> configures) {
		this.conf = configures;
	}
	
	public String getString(String key,String def){
		String val = conf.get(key);
		return StringKit.isNull(val)? 	 def:val;
	}
	
	public int getInt(String key,int def){
		String val = conf.get(key);
		return StringKit.isNull(val)? 	 def:Integer.parseInt(val);
	}
	
	public long getLong(String key,long def){
		String val = conf.get(key);
		return StringKit.isNull(val)? 	 def:Long.parseLong(val);
	}
	
	public double getDouble(String key,double def){
		String val = conf.get(key);
		return StringKit.isNull(val)? 	 def:Double.parseDouble(val);
	}
	
	public float getFloat(String key,float def){
		String val = conf.get(key);
		return StringKit.isNull(val)? 	 def:Float.parseFloat(val);
	}
	
}
