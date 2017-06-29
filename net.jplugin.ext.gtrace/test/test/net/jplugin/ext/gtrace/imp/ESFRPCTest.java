package test.net.jplugin.ext.gtrace.imp;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.kits.GTraceKit;
import net.jplugin.ext.webasic.impl.ESFHelper;
import net.jplugin.ext.webasic.impl.ESFRPCContext;

public class ESFRPCTest {

	public void test() throws InterruptedException, ExecutionException {
		ExecutorService svc = Executors.newFixedThreadPool(1);
		Future<Object> f = svc.submit(() -> {
			ESFRPCContext ctx = new ESFRPCContext();
			ctx.setCallerIpAddress("127.0.0.1");
			ctx.setClientAppCode("code1");
			ctx.setClientAppToken("tktktk");
			ctx.setGlobalReqId("aaaa#bbbb");
			ctx.setMsgReceiveTime(0);
			ctx.setTenantId("tn1");

			try {
				return ESFHelper.invokeWithRule(ctx, "/abcde", ESFRPCTest.this,
						ReflactKit.findSingeMethodExactly(ESFRPCTest.class, "testRPC"), new Object[] {});
			} catch (Throwable th) {
				th.printStackTrace();
				return false;
			}
		});
		
		Boolean result = (Boolean) f.get();
		AssertKit.assertTrue(result);
	}

	public boolean testRPC() {
		String tid = ThreadLocalContextManager.getRequestInfo().getTraceId();
		String sid  = ThreadLocalContextManager.getRequestInfo().getParSpanId();
		AssertKit.assertEqual(tid, "aaaa");
		AssertKit.assertEqual(sid, "bbbb");
		String sid2 = GTraceKit.getCurrentSpan(ThreadLocalContextManager.getCurrentContext()).getParentId();
		AssertKit.assertEqual(sid2, "bbbb");
		return true;
	}

}
