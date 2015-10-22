package net.jplugin.core.rclient;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.rclient.api.IClientHandler;
import net.jplugin.core.rclient.handler.JavaRemotHandler;

public class ExtendsionClientHelper {

	public static void addClientHandlerExtension(AbstractPlugin plugin, String protocol, Class clazz) {
		plugin.addExtension(Extension.create(net.jplugin.core.rclient.Plugin.EP_CLIENT_HANDLER, protocol,clazz));
	}
	public static void addServiceUrlResolverExtension(AbstractPlugin plugin, String protocol, Class clazz) {
		plugin.addExtension(Extension.create(net.jplugin.core.rclient.Plugin.EP_SERVICEURL_RESOLVER, protocol,clazz));
	}
	public static void addClientFilterExtension(AbstractPlugin plugin, Class clazz) {
		plugin.addExtension(Extension.create(net.jplugin.core.rclient.Plugin.EP_CLIENT_FILTER, clazz));
	}

}
