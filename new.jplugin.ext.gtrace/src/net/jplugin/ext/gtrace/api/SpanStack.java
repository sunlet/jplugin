package net.jplugin.ext.gtrace.api;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.RequestIdKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class SpanStack {
	List<Span> list=new ArrayList(5);

	public Span getCurrent() {
		int s = list.size();
		if (s < 1)
			throw new RuntimeException("No span.");
		return list.get(s-1);
	}
	
	public Span pushSpan(int spanType){
		String pid;
		if (list.isEmpty()){
			pid = ThreadLocalContextManager.instance.getContext().getRequesterInfo().getParSpanId();
		}else{
			pid = list.get(list.size()-1).getId();
		}
		Span span = new Span(spanType,RequestIdKit.newSpanId(),pid);
		list.add(span);
		return span;
	}

	public void popSpan() {
		if (list.isEmpty()){
			throw new RuntimeException("No span.");
		}else{
			list.remove(list.size()-1);
		}
		
	}
}
