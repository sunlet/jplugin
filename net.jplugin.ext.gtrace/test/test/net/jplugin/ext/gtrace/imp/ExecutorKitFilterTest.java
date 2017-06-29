package test.net.jplugin.ext.gtrace.imp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.kernel.kits.ExecutorKit;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.kits.GTraceKit;

public class ExecutorKitFilterTest {
	static ExecutorService svc = ExecutorKit.newFixedThreadPool(5);
	String outterTraceId;
	String parSpanId;
	public void test() throws InterruptedException, ExecutionException {
		Callable<Boolean> c = new Callable(){

			public Boolean call() {
				String traceId = ThreadLocalContextManager.getRequestInfo().getTraceId();
				Span span = GTraceKit.getCurrentSpan(ThreadLocalContextManager.instance.getContext());
				
				if (traceId==null) return false;
				if (span==null) return false;
				
				if (!outterTraceId.equals(traceId))
					return false;
				
				if (!parSpanId.equals(span.getParentId())) 
					return false;
				
//				if (id1.equals(id2)) return false;
				return true;
			}};
//		svc.submit(()->{});
		outterTraceId = ThreadLocalContextManager.getRequestInfo().getTraceId();
		parSpanId = GTraceKit.getCurrentSpan(ThreadLocalContextManager.getCurrentContext()).getId();
			
		Future<Boolean> ret = svc.submit(c);
		AssertKit.assertTrue(ret.get());
		System.out.println("get ok!");
		ret = svc.submit(c);
		AssertKit.assertTrue(ret.get());
		System.out.println("get ok!");
	}

}
