package net.jplugin.ext.gtrace.impl;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.RequestIdKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext;
import net.jplugin.common.kits.http.filter.IHttpClientFilter;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.api.SpanStack;
import net.jplugin.ext.gtrace.kits.GTraceKit;
import net.jplugin.ext.webasic.impl.InitRequestInfoFilterNew;


public class HttpClientFilter4TraceLog implements IHttpClientFilter {

	@Override
	public Object filter(FilterChain fc, HttpClientFilterContext ctx) throws Throwable {
		Map<String, String> map = ctx.getHeaders();
		if (map==null) {
			map = new HashMap<String, String>();
			ctx.setHeaders(map);
		}

		SpanStack ss = GTraceKit.getOrCreateSpanStack(ThreadLocalContextManager.getCurrentContext());
		Span span = ss.pushSpan(Span.CALLOUT);
		try{
			String traceId = ThreadLocalContextManager.getRequestInfo().getTraceId();
			String reqId = RequestIdKit.makeReqId(traceId,span.getId());
			
			if (StringKit.isNotNull(reqId)){
				map.put(InitRequestInfoFilterNew._REQID, reqId);
			}
			return fc.next(ctx);
		}finally{
			ss.popSpan();
		}
	}

	

}
