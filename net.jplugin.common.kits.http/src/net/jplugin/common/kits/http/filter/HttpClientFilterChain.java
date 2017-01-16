package net.jplugin.common.kits.http.filter;

import java.io.IOException;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.common.kits.http.filter.HttpFilterContext.Method;

public class HttpClientFilterChain {
	HttpClientFilterChain next;
	IHttpClientFilter filter;
	public void setNext(HttpClientFilterChain c){
		this.next = c;
	}
	
	public String next(HttpFilterContext ctx) throws IOException, HttpStatusException{
		if (this.next==null){
			//在这里验证，因为filter过程可能修改
			validate(ctx);
			//call
			if (ctx.getMethod()==HttpFilterContext.Method.POST){
				return HttpKit._post(ctx.getUrl(), ctx.getParams());
			}else{
				return HttpKit._get(ctx.getUrl());
			}
		}else{
			return this.next.filter.filter(this.next,ctx);
		}
	}

	private void validate(HttpFilterContext ctx) {
		Method m = ctx.getMethod();
		Map<String, Object> params = ctx.getParams();
		//validate
		AssertKit.assertNotNull(m, "http method");
		//get, params must empty
		AssertKit.assertTrue(m==Method.POST || (m==Method.GET && (params==null || params.isEmpty())));
	}
	
}
