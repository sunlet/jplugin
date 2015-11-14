package net.jplugin.core.rclient.handler;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.rclient.api.ClientCallContext;
import net.jplugin.core.rclient.api.IClientFilter;

public class ClientFilterRegistry {
	public static ClientFilterRegistry instance = new ClientFilterRegistry();
	private  IClientFilter[] filters=null;
	
	private ClientFilterRegistry(){}

	public void init(){
		filters = PluginEnvirement.getInstance().getExtensionObjects(net.jplugin.core.rclient.Plugin.EP_CLIENT_FILTER,IClientFilter.class);
	}
	
	public void filterStart(ClientCallContext ctx){
		for (IClientFilter cf:filters){
			cf.filterStart(ctx);
		}
	}
	public void filterEnd(ClientCallContext ctx){
		for (IClientFilter cf:filters){
			cf.filterEnd(ctx);
		}
	}
	
}
