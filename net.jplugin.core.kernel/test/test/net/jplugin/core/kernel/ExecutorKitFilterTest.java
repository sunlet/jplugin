package test.net.jplugin.core.kernel;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.kernel.kits.ExecutorKit;

public class ExecutorKitFilterTest {
	static ExecutorService svc = ExecutorKit.newFixedThreadPool(5);
	public void test() throws InterruptedException, ExecutionException {
		
		
		Callable<Boolean> c = new Callable(){

			public Boolean call() {
				String id1 = ThreadLocalContextManager.getRequestInfo().getRequestId();
				String id2 = ThreadLocalContextManager.getRequestInfo().getParentReqId();
				if (id1==null) return false;
				if (id2==null) return false;
				if (id1.equals(id2)) return false;
				return true;
			}};
//		svc.submit(()->{});
		Future<Boolean> ret = svc.submit(c);
		AssertKit.assertTrue(ret.get());
		System.out.println("get ok!");
		ret = svc.submit(c);
		AssertKit.assertTrue(ret.get());
		System.out.println("get ok!");
	}

}
