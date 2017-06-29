package net.jplugin.ext.gtrace.impl;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.api.SpanStack;
import net.jplugin.ext.gtrace.kits.GTraceKit;
import net.jplugin.ext.webasic.api.esf.IESFRestFilter;
import net.jplugin.ext.webasic.impl.ESFRestContext;
import net.jplugin.ext.webasic.impl.InitRequestInfoFilterNew;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;

public class ESFRestFilter4Trace implements IESFRestFilter {
	@Override
	public Object filter(FilterChain fc, Tuple2<ESFRestContext, CallParam> fctx) throws Throwable {
		// ����reqId
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		String reqId = fctx.first.getHeaderMap().get(InitRequestInfoFilterNew._REQID);
		GTraceKit.setTraceAndSpan(ctx.getRequesterInfo(),reqId);
		
		SpanStack ss = GTraceKit.getOrCreateSpanStack(ctx);
		ss.pushSpan(Span.EXPORT);
		try {
			return fc.next(fctx);
		} finally {
			ss.popSpan();
		}
	}

}
