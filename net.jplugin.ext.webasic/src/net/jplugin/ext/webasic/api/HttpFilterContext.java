package net.jplugin.ext.webasic.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpFilterContext {
	HttpServletRequest request;
	HttpServletResponse response;
	
	private HttpFilterContext(){}
	
	public static HttpFilterContext create (HttpServletRequest req,HttpServletResponse res){
		HttpFilterContext r = new HttpFilterContext();
		r.request = req;
		r.response = res;
		return r;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
