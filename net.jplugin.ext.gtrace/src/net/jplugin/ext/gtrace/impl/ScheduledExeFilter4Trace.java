package net.jplugin.ext.gtrace.impl;

import net.jplugin.common.kits.RequestIdKit;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.IScheduledExecutionFilter;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.api.SpanStack;
import net.jplugin.ext.gtrace.kits.GTraceKit;
import net.jplugin.ext.webasic.impl.InitRequestInfoFilterNew;

public class ScheduledExeFilter4Trace implements IScheduledExecutionFilter {

	@Override
	public Object filter(FilterChain fc, Object fctx) throws Throwable {
		//每次Scheduler都是一个root
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		GTraceKit.setTraceAndSpan(ctx.getRequesterInfo(),RequestIdKit.newTraceId());
		
		SpanStack ss = GTraceKit.getOrCreateSpanStack(ctx);
		ss.pushSpan(Span.SCHDULE_EXEC);
		try {
			return fc.next(fctx);
		} finally {
			ss.popSpan();
		}
	}

}
