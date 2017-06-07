package net.jplugin.ext.webasic.impl;

import java.util.HashMap;

import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class ESFRestContext {
	String callerIpAddress;
	String requestUrl;
	HashMap<String,String> cookieMap;
	HashMap<String,String> headerMap;

	public String getCallerIpAddress() {
		return callerIpAddress;
	}

	public void setCallerIpAddress(String callerIpAddress) {
		this.callerIpAddress = callerIpAddress;
	}

	public HashMap<String, String> getHeaderMap() {
		return headerMap;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public HashMap<String, String> getCookieMap() {
		return cookieMap;
	}

	public void setCookieMap(HashMap<String, String> cookieMap) {
		this.cookieMap = cookieMap;
	}
	
	public void setHeaderMap(HashMap<String, String> map) {
		this.headerMap = map;
	}

}
