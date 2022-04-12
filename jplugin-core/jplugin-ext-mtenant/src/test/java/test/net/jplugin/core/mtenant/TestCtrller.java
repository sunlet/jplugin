package test.net.jplugin.core.mtenant;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class TestCtrller{

	public String test(){
		String tid = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		if ("test111".equals(tid)) return "OK";
		else return "ERROR";
	}
}
