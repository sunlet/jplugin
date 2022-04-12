package net.jplugin.core.kernel.kits.scheduled;

import net.jplugin.common.kits.RequestIdKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class ScheduledRunnableWrapper implements Runnable{
	private Runnable inner;
	ScheduledRunnableWrapper(Runnable o){
		this.inner = o;
	}
	@Override
	public void run() {
		try{
			ThreadLocalContext ctx = ThreadLocalContextManager.instance.createContext();
			ctx.getRequesterInfo().setTraceId(RequestIdKit.newTraceId());
			ScheduledFilterManager.filter.filter(inner);
		}finally{
			ThreadLocalContextManager.instance.releaseContext();
		}
	}
}
