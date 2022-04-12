package net.jplugin.core.rclient.handler;

import java.util.Map;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.rclient.api.IClientFailHandler;

public class ClientFailHandlerManager {
	static Map<String, IClientFailHandler> map;
	public static void init(){
		 map = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.core.rclient.Plugin.EP_CLIENTFAIL_HANDLER,IClientFailHandler.class);
	}
	
	public static void connectFailed(String protocol,String url){
		IClientFailHandler h = map.get(protocol);
		if (h!=null) 
			h.connectFailed(url);
	}
}
