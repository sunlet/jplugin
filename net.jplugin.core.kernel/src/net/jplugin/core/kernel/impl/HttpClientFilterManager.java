package net.jplugin.core.kernel.impl;

import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.filter.FilterManager;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext.Method;
import net.jplugin.core.kernel.api.PluginFilterManager;

public class HttpClientFilterManager {
	static PluginFilterManager<HttpClientFilterContext> filterManager = new PluginFilterManager<>(
			net.jplugin.core.kernel.Plugin.EP_HTTP_CLIENT_FILTER,
			(fc,ctx)->{ 
				//在这里验证，因为filter过程可能修改
				validate(ctx);
				//call
				if (ctx.getMethod()==HttpClientFilterContext.Method.POST){
					return HttpKit._post(ctx.getUrl(), ctx.getParams(),ctx.getHeaders());
				}else{
					return HttpKit._get(ctx.getUrl(),ctx.getHeaders());
				}
				
			});
	
	public static void init(){
		filterManager.init();
		HttpKit._setHttpFilterManager(filterManager);
	}
	
	private static void validate(HttpClientFilterContext ctx) {
		Method m = ctx.getMethod();
		Map<String, Object> params = ctx.getParams();
		//validate
		AssertKit.assertNotNull(m, "http method");
		//get, params must empty
		AssertKit.assertTrue(m==Method.POST || (m==Method.GET && (params==null || params.isEmpty())));
	}
}
