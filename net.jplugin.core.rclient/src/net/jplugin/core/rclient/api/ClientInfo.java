package net.jplugin.core.rclient.api;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-13 下午01:37:33
 **/

public class ClientInfo {
	private String appId;
	private String appToken;
	private HashMap<String,String> extParas;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppToken() {
		return appToken;
	}
	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}
	public Map<String,String> getExtParas(){
		return extParas;
	}
	public void setExtPara(String key,String val){
		if (extParas==null) extParas = new HashMap<String, String>();
		extParas.put(key, val);
	}
	
}
