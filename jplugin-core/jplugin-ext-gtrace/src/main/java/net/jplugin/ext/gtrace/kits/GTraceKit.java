package net.jplugin.ext.gtrace.kits;

import net.jplugin.common.kits.RequestIdKit;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.api.SpanStack;

public class GTraceKit {
	public static final String ATTR_SPAN_STACK="$span-stack";
	
	public static Span getCurrentSpan(){
		return getCurrentSpan(ThreadLocalContextManager.getCurrentContext());
	}
	
	public static Span getCurrentSpan(ThreadLocalContext ctx){
		SpanStack ss = (SpanStack) ctx.getAttribute(ATTR_SPAN_STACK);
		if (ss==null) return null;
		else return ss.getCurrent();
	}
	
	public static void setTraceAndSpan(RequesterInfo info, String greqid) {
		if (greqid!=null){
			Tuple2<String, String> tuple2 = RequestIdKit.parse(greqid);
			info.setTraceId(tuple2.first);
			info.setParSpanId(tuple2.second);
		}else{
			info.setTraceId(RequestIdKit.newTraceId());
			info.setParSpanId(null);
		}
	}
	
	public static SpanStack getOrCreateSpanStack(ThreadLocalContext ctx) {
		SpanStack ss = (SpanStack) ctx.getAttribute(ATTR_SPAN_STACK);
		if (ss==null) {
			ss = new SpanStack();
			ctx.setAttribute(ATTR_SPAN_STACK, ss);
		}
		return ss;
	}
}
