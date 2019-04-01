package test.net.jplugin.core.mtenant.iterator;

import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.mtenant.api.TenantIteratorKit;
import net.jplugin.core.mtenant.api.TenantResult;

public class TenantIteratorTest {

	public void testRunnable() {

		List<TenantResult> list = TenantIteratorKit.execute(() -> {
			String tid = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
			if ("1001".equals(tid))
				;// donothing
			else
				throw new RuntimeException("ex for 1002");
		});
		
		AssertKit.assertEqual(list.size(), 2);
		
		for (TenantResult r:list){
			AssertKit.assertNull(r.getResult());
			if ("1001".equals(r.getTenantId())){
				AssertKit.assertNull(r.getThrowable());
			}else{
				AssertKit.assertNotNull(r.getThrowable(),"");
			}
		}

	}
	
	public void testCallable(){

		List<TenantResult> list = TenantIteratorKit.execute(() -> {
			String tid = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
			if ("1001".equals(tid))
				return 1;
			else
				throw new RuntimeException("ex for 1002");
		});
		
		
		for (TenantResult r:list){
			if ("1001".equals(r.getTenantId())){
				AssertKit.assertNull(r.getThrowable());
				AssertKit.assertEqual(1,r.getResult());
			}else{
				AssertKit.assertNotNull(r.getThrowable(),"");
				AssertKit.assertNull(r.getResult());
			}
		}
	}

}
