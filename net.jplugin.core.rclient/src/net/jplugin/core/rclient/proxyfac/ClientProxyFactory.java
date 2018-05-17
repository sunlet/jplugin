package net.jplugin.core.rclient.proxyfac;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.rclient.api.ClientFactory;
import net.jplugin.core.rclient.api.ClientInfo;

public class ClientProxyFactory {

	public static ClientProxyFactory instance=new ClientProxyFactory();
	
	Map<String,Object > objectMap=new Hashtable<String, Object>();
	private Map<String, ClientProxyDefinition> defMap;
	
	public void init() {
		defMap = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.core.rclient.Plugin.EP_CLIENT_PROXY, ClientProxyDefinition.class);
//		
//		//check
//		Set<Class> tmp = new HashSet<Class>();
//		for ( ClientProxyDefinition o:defMap.values()){
//			Class c = o.getInterf();
//			if (tmp.contains(c)) throw new RuntimeException("One interface can only have one proxy now. "+c.getName());
//			tmp.add(c);
//		}
	}
	
	public  <T> T getClientProxy(Class<T> interf){
		return getClientProxy(interf.getName(),interf);
	}
	
	public <T> T getClientProxy(String localName,Class<T> interf){
		Object ret = objectMap.get(localName);
		if (ret==null){
			ClientProxyDefinition def = defMap.get(localName);
			
			if (def==null){
				throw new RuntimeException("Can't find the proxy definition: localName ="+localName);
			}
			
			ClientInfo ci = new ClientInfo();
			ci.setAppId(def.getAppId());
			ci.setAppToken(def.getAppToken());
			
			ret = ClientFactory.create(interf, def.getUrl(),def.getProtocol(),ci).getObject();
			objectMap.put(localName, ret);
		}
		return (T) ret;
	}

}
