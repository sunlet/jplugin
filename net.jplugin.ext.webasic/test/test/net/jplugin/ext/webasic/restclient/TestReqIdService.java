package test.net.jplugin.ext.webasic.restclient;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class TestReqIdService {
	public void test(){
		String r1 = ThreadLocalContextManager.getRequestInfo().getParentReqId();
		String r2 = ThreadLocalContextManager.getRequestInfo().getRequestId();
		AssertKit.assertEqual(r1,reqid);
		AssertKit.assertFalse(r1.equals(r2));
	}
	
	static String reqid;
	
	public static void calltest(){
		reqid = ThreadLocalContextManager.getRequestInfo().getRequestId();
		try{
			String ret = HttpKit.post("http://localhost:8080/demo/testReqId/test.do", new HashMap());
			Map result = JsonKit.json2Map(ret);
			AssertKit.assertEqual(result.get("success"), true);
		}catch(Exception e){
			throw new RuntimeException("test fail");
		}
	}
}
