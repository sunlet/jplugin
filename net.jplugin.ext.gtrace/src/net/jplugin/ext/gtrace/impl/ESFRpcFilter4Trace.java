package net.jplugin.ext.gtrace.impl;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.api.SpanStack;
import net.jplugin.ext.gtrace.kits.GTraceKit;
import net.jplugin.ext.webasic.api.InvocationContext;
import net.jplugin.ext.webasic.api.esf.IESFRpcFilter;
import net.jplugin.ext.webasic.impl.ESFRPCContext;

public class ESFRpcFilter4Trace implements IESFRpcFilter {

	@Override
	public Object filter(FilterChain fc, Tuple2<ESFRPCContext, InvocationContext> fctx) throws Throwable {
		//����reqId
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		String reqId = fctx.first.getGlobalReqId();
		GTraceKit.setTraceAndSpan(ctx.getRequesterInfo(),reqId);
		
		SpanStack ss = GTraceKit.getOrCreateSpanStack(ctx);
		ss.pushSpan(Span.EXPORT);
		try{
			return fc.next(fctx);
		}finally{
			ss.popSpan();
		}
	}

}
