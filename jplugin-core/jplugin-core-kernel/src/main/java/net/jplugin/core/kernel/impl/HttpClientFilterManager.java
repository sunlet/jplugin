package net.jplugin.core.kernel.impl;

import net.jplugin.common.kits.http.HttpKit.HttpExecution;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext;
import net.jplugin.core.kernel.api.PluginFilterManager;

public class HttpClientFilterManager {
	static PluginFilterManager<HttpClientFilterContext> filterManager = new PluginFilterManager<>(
			net.jplugin.core.kernel.Plugin.EP_HTTP_CLIENT_FILTER,
			(fc,ctx)->{ 
				return HttpExecution.execute(ctx);
			});
	
	public static void init(){
		filterManager.init();
		HttpKit._setHttpFilterManager(filterManager);
	}
}
