package net.jplugin.core.kernel.kits.scheduled;

import java.util.concurrent.Callable;

import net.jplugin.common.kits.RequestIdKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class ScheduledCallableWrapper implements Callable{
	private Callable inner;
	
	ScheduledCallableWrapper(Callable o){
		this.inner = o;
	}
	
	@Override
	public Object call() throws Exception {
		try{
			ThreadLocalContext ctx = ThreadLocalContextManager.instance.createContext();
			ctx.getRequesterInfo().setTraceId(RequestIdKit.newTraceId());
			return ScheduledFilterManager.filter.filter(inner);
//			return this.inner.call();
		}finally{
			ThreadLocalContextManager.instance.releaseContext();
		}
	}

}
