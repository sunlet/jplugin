package net.jplugin.core.kernel.api.ctx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Cookies {
	private HashMap<String,String> map;
	public String getCookie(String key){
		return (map==null)? null:map.get(key);
	}
	public void setCookie(String name,String val){
		if (map==null) map = new HashMap();
		map.put(name, val);
	}
	public Set<String> cookieKeys(){
		return (Set<String>) (map==null? Collections.emptySet():map.keySet());
	}
	public void _setFromMap(HashMap<String,String> m){
		this.map = m;
	}
	
	public String toString(){
		if (map==null) return "{}";
		StringBuffer sb  = new StringBuffer();
		for (Entry<String, String> en:map.entrySet()){
			sb.append(en.getKey()+"="+en.getValue());
			sb.append(",");
		}
		return sb.toString();
	}
}
