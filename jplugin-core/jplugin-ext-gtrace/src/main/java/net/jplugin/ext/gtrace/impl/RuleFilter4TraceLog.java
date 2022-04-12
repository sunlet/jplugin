package net.jplugin.ext.gtrace.impl;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.ctx.api.IRuleServiceFilter;
import net.jplugin.core.ctx.api.RuleServiceFilterContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.api.SpanStack;
import net.jplugin.ext.gtrace.kits.GTraceKit;

public class RuleFilter4TraceLog implements IRuleServiceFilter {

	@Override
	public Object filter(FilterChain fc, RuleServiceFilterContext fctx) throws Throwable {
		SpanStack ss = GTraceKit.getOrCreateSpanStack(ThreadLocalContextManager.getCurrentContext());
		ss.pushSpan(Span.INTERNAL_CALL);
		try {
			return fc.next(fctx);
		} finally {
			ss.popSpan();
		}
	}

}
