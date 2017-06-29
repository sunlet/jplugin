package net.jplugin.ext.gtrace.impl;

import net.jplugin.common.kits.RequestIdKit;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.kernel.api.IPluginEnvInitFilter;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.kits.GTraceKit;

public class PluginInitFilter4Trace implements IPluginEnvInitFilter{

	@Override
	public Object filter(FilterChain fc, Tuple2<Boolean, String> fctx) throws Throwable {
		ThreadLocalContext ctx = ThreadLocalContextManager.getCurrentContext();
		GTraceKit.getOrCreateSpanStack(ctx).pushSpan(Span.SYSTEM_INIT);
		ctx.getRequesterInfo().setTraceId(RequestIdKit.newTraceId());
		
		return fc.next(fctx);
	}

}
