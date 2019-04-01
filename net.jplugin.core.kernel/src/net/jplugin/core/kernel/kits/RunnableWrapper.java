package net.jplugin.core.kernel.kits;

import net.jplugin.common.kits.AttributedObject;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public  class RunnableWrapper extends AttributedObject implements Runnable {
	
	Runnable inner;
	private String tenantId;
//	public String traceId;
//	public String parSpanId;
	public RunnableWrapper(Runnable runnable){
		this.inner = runnable;

		RequesterInfo ri = ThreadLocalContextManager.getRequestInfo();
		this.tenantId = ri.getCurrentTenantId();
		
		RunnableInitFilterManager.filter(this);
		
		
//		//获取父线程状态变量
//		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
//		RequesterInfo r = ThreadLocalContextManager.getRequestInfo();
//		this.tenantId = r.getCurrentTenantId();
//		
//		//注意：traceId和parspanId目前难以在filter拦截器中实现，就在这里直接写了
//		this.traceId = r.getTraceId();
//		SpanStack ss = (SpanStack) ctx.getAttribute(ThreadLocalContext.ATTR_SPAN_STACK);
//		if (ss!=null) 
//			parSpanId = ss.getCurrent().getId();
	}
	public void run() {
		//子线程设置状态标量
		try{
			ThreadLocalContext ctx = ThreadLocalContextManager.instance.createContext();
			RequesterInfo r = ctx.getRequesterInfo();
			r.setCurrentTenantId(this.tenantId);
			
			//执行过滤器，并执行Message
			ExecutorKitFilterManager.filter(this);
		}finally{
			  ThreadLocalContextManager.instance.releaseContext();
		}
	}
	
}