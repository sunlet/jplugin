package net.jplugin.core.rclient.handler;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.rclient.api.IClientHandler;

public class ClientHandlerRegistry {
	public static ClientHandlerRegistry instance = new ClientHandlerRegistry();
	private Map<String, IClientHandler> map;
	
	private ClientHandlerRegistry(){}

	public void init(){
		Map<String, IClientHandler> extmap = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.core.rclient.Plugin.EP_CLIENT_HANDLER,IClientHandler.class);
		map = new HashMap<String, IClientHandler>();
		this.map.putAll(extmap);
	}
	
	public IClientHandler getClientHandler(String type){
		if (map == null){
			throw new RuntimeException("You must call PluginEnvirement.getInstance().startup() before use client");
		}
		return map.get(type);
	}
}
