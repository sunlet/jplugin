package net.jplugin.common.kits.http.filter;

import java.util.Map;

public class HttpFilterContext {
	public enum Method{POST,GET}
	Method method;
	String url;
	Map<String,Object> params;
	
	public HttpFilterContext(Method m,String u,Map<String,Object> p){
		this.method = m;
		this.url = u;
		this.params = p;
	}
	
	public Method getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public Map<String, Object> getParams() {
		return params;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
}
