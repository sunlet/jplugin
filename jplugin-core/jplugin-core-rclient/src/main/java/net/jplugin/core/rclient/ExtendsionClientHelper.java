package net.jplugin.core.rclient;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.rclient.api.IClientHandler;
import net.jplugin.core.rclient.handler.JavaRemotHandler;
import net.jplugin.core.rclient.proxyfac.ClientProxyDefinition;
import net.jplugin.core.rclient.proxyfac.ClientProxyFactory;

public class ExtendsionClientHelper {
	/**
	 * 注意：目前localName 为接口的名字
	 * @param plugin
	 * @param url
	 * @param protocol
	 * @param clazz
	 */
	public static void addClientProxyExtension(AbstractPlugin plugin,Class clazz,String url,String protocol){
//		plugin.addExtension(Extension.create(net.jplugin.core.rclient.Plugin.EP_CLIENT_PROXY,clazz.getName(), ClientProxyDefinition.class,new String[][]{{"protocol",protocol},{"interf",clazz.getName()},{"url",url}}));
		addClientProxyExtension(plugin,clazz,url,protocol,null);
	}
	public static void addClientProxyExtension(AbstractPlugin plugin,Class clazz,String url,String protocol,String appId){
		plugin.addExtension(Extension.create(net.jplugin.core.rclient.Plugin.EP_CLIENT_PROXY,clazz.getName(), ClientProxyDefinition.class,new String[][]{{"protocol",protocol},{"interf",clazz.getName()},{"appId",appId},{"url",url}}));
	}
	public static void addClientHandlerExtension(AbstractPlugin plugin, String protocol, Class clazz) {
		plugin.addExtension(Extension.create(net.jplugin.core.rclient.Plugin.EP_CLIENT_HANDLER, protocol,clazz));
	}
	public static void addServiceUrlResolverExtension(AbstractPlugin plugin, String protocol, Class clazz) {
		plugin.addExtension(Extension.create(net.jplugin.core.rclient.Plugin.EP_SERVICEURL_RESOLVER, protocol,clazz));
	}
	public static void addClientFilterExtension(AbstractPlugin plugin, Class clazz) {
		plugin.addExtension(Extension.create(net.jplugin.core.rclient.Plugin.EP_CLIENT_FILTER, clazz));
	}

	public static void addClientFailHandlerExtension(AbstractPlugin plugin, String protocol,Class clazz) {
		plugin.addExtension(Extension.create(net.jplugin.core.rclient.Plugin.EP_CLIENTFAIL_HANDLER, protocol,clazz));
	}
}
