package net.jplugin.core.rclient.handler;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.rclient.api.IServiceUrlResolver;

public class ServiceUrlResolverManager{

	public static ServiceUrlResolverManager instance = new ServiceUrlResolverManager();
	private Map<String, IServiceUrlResolver> map;
	
	private ServiceUrlResolverManager(){}

	public void init(){
		Map<String, IServiceUrlResolver> extmap = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.core.rclient.Plugin.EP_SERVICEURL_RESOLVER,IServiceUrlResolver.class);
		map = new HashMap<String, IServiceUrlResolver>();
		this.map.putAll(extmap);
	}
	
	public String resolveUrl(String protocol,String baseUrl){
		if (map == null){
			throw new RuntimeException("You must call PluginEnvirement.getInstance().startup() before this");
		}
		IServiceUrlResolver r = map.get(protocol);
		if (r==null) return baseUrl;
		else return r.resolve(protocol, baseUrl);
	}
}
