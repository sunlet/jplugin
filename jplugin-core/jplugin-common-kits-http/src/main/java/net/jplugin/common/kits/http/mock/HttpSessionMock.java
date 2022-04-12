package net.jplugin.common.kits.http.mock;

import java.util.HashMap;

public class HttpSessionMock extends HttpSessionEmpty {
	static HashMap<String, Object> attrMap = new HashMap<String, Object>();
	
	public static void set(String key,Object o){
		attrMap.put(key, o);
	}
	
	@Override
	public Object getAttribute(String s) {
		return attrMap.get(s);
	}
	
	@Override
	public void setAttribute(String key, Object val) {
		attrMap.put(key, val);
	}
}
