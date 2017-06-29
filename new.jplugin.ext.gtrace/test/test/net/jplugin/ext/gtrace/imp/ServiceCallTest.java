package test.net.jplugin.ext.gtrace.imp;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.kits.GTraceKit;

public class ServiceCallTest {
	public void test(){
		String r1 = ThreadLocalContextManager.getRequestInfo().getTraceId();
		Span span = GTraceKit.getCurrentSpan(ThreadLocalContextManager.getCurrentContext());
		
		AssertKit.assertEqual(r1,reqid);
		AssertKit.assertTrue(!span.getParentId().equals(parSpan));
	}
	
	static String reqid;
	static String parSpan;
	public static void calltest(){
		reqid = ThreadLocalContextManager.getRequestInfo().getTraceId();
		parSpan = GTraceKit.getCurrentSpan(ThreadLocalContextManager.getCurrentContext()).getId();
		
		try{
			String ret = HttpKit.post("http://localhost:8080/demo/testReqId/test.do", new HashMap());
			Map result = JsonKit.json2Map(ret);
			AssertKit.assertEqual(result.get("success"), true);
		}catch(Exception e){
			throw new RuntimeException("test fail");
		}
	}
}
