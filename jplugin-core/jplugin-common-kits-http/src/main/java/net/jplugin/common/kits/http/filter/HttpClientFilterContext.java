package net.jplugin.common.kits.http.filter;

import java.util.Map;

public class HttpClientFilterContext {
	public enum Method{POST,GET, PUT, DELETE}
	Method method;
	String url;
	Map<String,Object> params;
	Map<String,String> headers;
	
	public HttpClientFilterContext(Method m,String u,Map<String,Object> p){
		this(m,u,p,null);
	}
	public HttpClientFilterContext(Method m,String u,Map<String,Object> aParams,Map<String,String> aHeaders){
		this.method = m;
		this.url = u;
		this.params = aParams;
		this.headers = aHeaders;
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

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
}
