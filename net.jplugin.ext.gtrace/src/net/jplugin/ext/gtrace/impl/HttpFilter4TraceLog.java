package net.jplugin.ext.gtrace.impl;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.api.SpanStack;
import net.jplugin.ext.gtrace.kits.GTraceKit;
import net.jplugin.ext.webasic.api.IHttpFilter;

public class HttpFilter4TraceLog implements IHttpFilter{

	public static final String _REQID = "_reqid_";

	@Override
	public Object filter(FilterChain fc, net.jplugin.ext.webasic.api.HttpFilterContext ctx) throws Throwable {
		
		//����reqId
		ThreadLocalContext tctx = ThreadLocalContextManager.instance.getContext();
		RequesterInfo requestInfo = tctx.getRequesterInfo();
		String reqId = (String) ctx.getRequest().getHeader(_REQID);
		GTraceKit.setTraceAndSpan(requestInfo,reqId);
		
		SpanStack ss = GTraceKit.getOrCreateSpanStack(tctx);
		ss.pushSpan(Span.EXPORT);
		try{
			return fc.next(ctx);
		}finally{
			ss.popSpan();
		}
	}

}
