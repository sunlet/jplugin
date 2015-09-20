package net.jplugin.core.rclient.api;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-13 下午01:37:33
 **/

public class ClientInfo {
	private String username;
	private String token;
	private HashMap<String,String> extParas;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Map<String,String> getExtParas(){
		return extParas;
	}
	public void setExtPara(String key,String val){
		if (extParas==null) extParas = new HashMap<String, String>();
		extParas.put(key, val);
	}
	
}
