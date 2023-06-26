package test.net.jplugin.ext.gtrace.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.ext.webasic.impl.ESFHelper;
import net.jplugin.ext.webasic.impl.ESFRestContext;
import net.jplugin.ext.webasic.impl.InitRequestInfoFilterNew;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;

public class ESFRestTest {

	public void test() throws InterruptedException, ExecutionException {
		ExecutorService svc = Executors.newFixedThreadPool(1);
		Future<Object> f = svc.submit(() -> {
			ESFRestContext ctx = new ESFRestContext();
			ctx.setCallerIpAddress("127.0.0.1");
			ctx.setRequestUrl("http://ssssss/esfRestTest.do");
			HashMap<String, String> head = new HashMap<String, String>();
			head.put(InitRequestInfoFilterNew._REQID, "aaaa#bbbb");
			ctx.setHeaderMap(head);

			Map<String, String> m = new HashMap<>();
			m.put("a", "a");
			m.put("b", "b");
			CallParam cp = CallParam.create("/esfRestTest", "add", m);
			try {
				ESFHelper.callRestfulService(ctx, cp);
				String result = cp.getResult();
				Map mmm = JsonKit.json2Map(result);
				Object rrr = ((Map) mmm.get("content")).get("result");
				if ("ab".equals(rrr))
					return true;
				else
					return false;
			} catch (Throwable th) {
				th.printStackTrace();
				return false;
			}
		});

		Boolean result = (Boolean) f.get();
		AssertKit.assertTrue(result);
	}

}
