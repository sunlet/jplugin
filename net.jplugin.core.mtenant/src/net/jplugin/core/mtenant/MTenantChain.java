package net.jplugin.core.mtenant;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext.Method;
import net.jplugin.common.kits.http.filter.IHttpClientFilter;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.config.api.RefConfig;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.webasic.impl.MtInvocationFilterHandler;


public class MTenantChain  extends RefAnnotationSupport implements IHttpClientFilter{

	private String reqParamName;

	public  MTenantChain() {
		reqParamName = ConfigFactory.getStringConfig("mtenant.req-param-name");
	}
	
	@RefConfig(defaultValue="true",path="mtenant.tenantid-inbody-for-comp-httpkit")
	private Boolean tenantIdInBodyForCompatible;
	
	@Override
	public Object filter(FilterChain fc, HttpClientFilterContext ctx) throws Throwable {
		String tid = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		if (StringKit.isNull(tid)){
			return fc.next(ctx);
		}
		//放在header里面
		setHeader(ctx,tid);
		
		//如果有兼容要求，放在req里面
		if (tenantIdInBodyForCompatible){
			Method method = ctx.getMethod();
			if (method == HttpClientFilterContext.Method.POST || method == HttpClientFilterContext.Method.PUT){
				handlePost(ctx,tid);
			}else if (method == HttpClientFilterContext.Method.GET || method == HttpClientFilterContext.Method.DELETE){
				handleGet(ctx,tid);
			}
		}
		
		return fc.next(ctx);
	}

	private void setHeader(HttpClientFilterContext ctx, String tid) {
		Map<String, String> h = ctx.getHeaders();
		if (h==null){
			h= new HashMap();
			h.put(MtInvocationFilterHandler.TENANT_ID,tid);
			ctx.setHeaders(h);
		}else{
			h.put(MtInvocationFilterHandler.TENANT_ID,tid);
		}
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
