package net.jplugin.core.mtenant;

import java.util.HashMap;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext;
import net.jplugin.common.kits.http.filter.IHttpClientFilter;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;


public class MTenantChain implements IHttpClientFilter {

	private String reqParamName;

	public  MTenantChain() {
		reqParamName = ConfigFactory.getStringConfig("mtenant.req-param-name");
	}
	
	@Override
	public Object filter(FilterChain fc, HttpClientFilterContext ctx) throws Throwable {
		String tid = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		if (StringKit.isNull(tid)){
			return fc.next(ctx);
		}
		
		if (ctx.getMethod() == HttpClientFilterContext.Method.POST){
			handlePost(ctx,tid);
		}else if (ctx.getMethod() == HttpClientFilterContext.Method.GET){
			handleGet(ctx,tid);
		}
		return fc.next(ctx);
	}

	private void handlePost(HttpClientFilterContext ctx,String tid) {
		if (ctx.getParams()==null) 
			ctx.setParams(new HashMap<String,Object>());
		ctx.getParams().put(reqParamName, tid);
	}
	private void handleGet(HttpClientFilterContext ctx,String tid) {
		String url = ctx.getUrl();
		if (StringKit.isNull(url))
			throw new RuntimeException("Get url is null");
		if (url.indexOf("?")>0){ 
			url = url + "&";
		}else{
			url = url +"?";
		}
		url = url + reqParamName+"="+tid;
		ctx.setUrl(url);
	}

}
