package net.jplugin.core.ctx.api;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-2-4 上午10:17:16
 **/

public class RuleParameter {
	private HashMap<String, String> map = new HashMap<String, String>();


	
	public HashMap<String, String> getParameterMap(){
		return this.map;
	}
	
	public String getString(String key) {
		return map.get(key);
	}

	public long getLong(String key) {
		assertExist(key);
		return Long.parseLong(map.get(key));
	}
	
	public int getInt(String key) {
		assertExist(key);
		return Integer.parseInt(key);
	}

	public double getDouble(String key) {
		assertExist(key);
		return Double.parseDouble(key);
	}

	public double getFloat(String key) {
		assertExist(key);
		return Float.parseFloat(key);
	}

	/**
	 * @param key
	 */
	private void assertExist(String key) {
		if (!map.containsKey(key))
			throw new RuntimeException("Can't find the key:"+key);
	}

}
