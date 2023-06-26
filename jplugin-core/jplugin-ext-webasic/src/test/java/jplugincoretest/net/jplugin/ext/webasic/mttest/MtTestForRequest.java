package jplugincoretest.net.jplugin.ext.webasic.mttest;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class MtTestForRequest {
	public String testGetTenantId(){
		String tid = ThreadLocalContextManager.getCurrentContext().getRequesterInfo().getCurrentTenantId();
		return tid;
	}
}
