package net.jplugin.ext.gtrace.api;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.RequestIdKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class SpanStack {
	List<Span> list=new ArrayList(5);

	public Span getCurrent() {
		int s = list.size();
		if (s < 1){
//			throw new RuntimeException("No span.");
			//提高容错性，这里push一个默认的span
			return pushSpan(Span.SPAN_DEFAULT);
		}
		return list.get(s-1);
	}
	
	public Span pushSpan(int spanType){
		String pid;
		int index;
		if (list.isEmpty()){
			ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
			pid = ctx.getRequesterInfo().getParSpanId();
			index = addChildIndex(ctx); 
		}else{
			Span parentSpan = list.get(list.size()-1);
			pid = parentSpan.getId();
			index = addChildIndex(parentSpan);
		}
		Span span = new Span(spanType,index,pid);
		list.add(span);
		return span;
	}

	private int addChildIndex(Span parentSpan) {
		int idx = parentSpan.getLastChildIndex();
		idx++;
		parentSpan.setLastChildIndex(idx);
		return idx;
	}

	private static final String TRACE_CHILD_SPAN_INDEX="TRACE_CHILD_SPAN_INDEX";
	private int addChildIndex(ThreadLocalContext ctx) {
		Integer childSpanIndex = (Integer)ctx.getAttribute(TRACE_CHILD_SPAN_INDEX);
		if (childSpanIndex==null){
			childSpanIndex = 0;
		}
		childSpanIndex++;
		ctx.setAttribute(TRACE_CHILD_SPAN_INDEX, childSpanIndex);
		return childSpanIndex;
	}

	public void popSpan() {
		if (list.isEmpty()){
			throw new RuntimeException("No span.");
		}else{
			list.remove(list.size()-1);
		}
		
	}
}
