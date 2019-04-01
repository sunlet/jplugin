package net.jplugin.ext.gtrace.impl;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.IExeRunnableInitFilter;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.kernel.kits.RunnableWrapper;
import net.jplugin.ext.gtrace.api.SpanStack;
import net.jplugin.ext.gtrace.kits.GTraceKit;

public class RunnableInitFilter4Trace implements IExeRunnableInitFilter {

	@Override
	public Object filter(FilterChain fc, RunnableWrapper o) throws Throwable {
		//获取父线程状态变�?
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		RequesterInfo r = ThreadLocalContextManager.getRequestInfo();
		
		//注意：traceId和parspanId目前难以在filter拦截器中实现，就在这里直接写�?
		o.setAttribute(RunWrapperConstants.TRACE_ID, r.getTraceId());
		SpanStack ss = (SpanStack) ctx.getAttribute(GTraceKit.ATTR_SPAN_STACK);
		if (ss!=null) 
			o.setAttribute(RunWrapperConstants.PAR_SPAN_ID,ss.getCurrent().getId());
		
		return fc.next(o);
	}

}
