package net.jplugin.core.mtenant.tidv;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.ext.webasic.api.IInvocationFilter;
import net.jplugin.ext.webasic.api.InvocationContext;

public class TenantIDValidator implements IInvocationFilter{
	
	private static boolean nullTenantAllowed; //default value is true
	public static void init(){
		nullTenantAllowed = !"false".equalsIgnoreCase(ConfigFactory.getStringConfigWithTrim("mtenant.null-tenant-allowed"));
		PluginEnvirement.INSTANCE.getStartLogger().log("@@@mtenant.null-tenant-allowed="+nullTenantAllowed);
	}
	
	@Override
	public boolean before(InvocationContext ctx) {
		if (nullTenantAllowed) 
			return true;
		
		if (ThreadLocalContextManager.getCurrentContext().getRequesterInfo().getCurrentTenantId()==null){
			throw new RemoteExecuteException(RemoteExecuteException.ERROR_NO_TENANTID,"no tenant id."+ThreadLocalContextManager.getCurrentContext().getRequesterInfo().getRequestUrl());
		}
		
		return true;
	}

	@Override
	public void after(InvocationContext ctx) {
	}

}
