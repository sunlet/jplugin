package net.jplugin.ext.gtrace.impl;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.IExecutorFilter;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.kernel.kits.RunnableWrapper;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.api.SpanStack;
import net.jplugin.ext.gtrace.kits.GTraceKit;

public class ExecuterFilter4TraceLog implements IExecutorFilter{

	@Override
	public Object filter(FilterChain fc, RunnableWrapper wrapper) throws Throwable {
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		RequesterInfo r = ctx.getRequesterInfo();
		r.setParSpanId((String) wrapper.getAttribute(RunWrapperConstants.PAR_SPAN_ID));
		r.setTraceId((String) wrapper.getAttribute(RunWrapperConstants.TRACE_ID));

		SpanStack ss = GTraceKit.getOrCreateSpanStack(ctx);
		ss.pushSpan(Span.EXPORT);
		try{
			return fc.next(wrapper);
		}finally{
			ss.popSpan();
		}
	}

}
