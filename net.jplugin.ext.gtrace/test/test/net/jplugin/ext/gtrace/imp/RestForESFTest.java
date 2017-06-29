package test.net.jplugin.ext.gtrace.imp;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.kits.GTraceKit;
import net.jplugin.ext.webasic.api.Para;

public class RestForESFTest {
	public String add(@Para(name="a") String a,@Para(name="b") String b){
		String tid = ThreadLocalContextManager.getRequestInfo().getTraceId();
		String sid  = ThreadLocalContextManager.getRequestInfo().getParSpanId();
		AssertKit.assertEqual(tid, "aaaa");
		AssertKit.assertEqual(sid, "bbbb");
		String sid2 =  GTraceKit.getCurrentSpan(ThreadLocalContextManager.getCurrentContext()).getParentId();
		AssertKit.assertEqual(sid2, "bbbb");
		
		return a+b;
	}
}
