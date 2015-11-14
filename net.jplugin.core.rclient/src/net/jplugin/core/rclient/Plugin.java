package net.jplugin.core.rclient;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientFactory;
import net.jplugin.core.rclient.api.IClientFilter;
import net.jplugin.core.rclient.api.IClientHandler;
import net.jplugin.core.rclient.api.IServiceUrlResolver;
import net.jplugin.core.rclient.handler.ClientFilterRegistry;
import net.jplugin.core.rclient.handler.ClientHandlerRegistry;
import net.jplugin.core.rclient.handler.JavaRemotHandler;
import net.jplugin.core.rclient.handler.RestHandler;
import net.jplugin.core.rclient.handler.ServiceUrlResolverManager;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-14 ����09:35:53
 **/

public class Plugin extends AbstractPlugin{
	public static final String EP_CLIENT_HANDLER ="EP_CLIENT_HANDLER";
	public static final String EP_CLIENT_FILTER ="EP_CLIENT_FILTER";
	public static final String EP_SERVICEURL_RESOLVER = "EP_SERVICEURL_RESOLVER";
	
	
	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.create(EP_CLIENT_HANDLER, IClientHandler.class, true));
		this.addExtensionPoint(ExtensionPoint.create(EP_SERVICEURL_RESOLVER, IServiceUrlResolver.class, true));
		this.addExtensionPoint(ExtensionPoint.create(EP_CLIENT_FILTER, IClientFilter.class));
		
		ExtendsionClientHelper.addClientHandlerExtension(this,Client.PROTOCOL_REMOJAVA,JavaRemotHandler.class);
		ExtendsionClientHelper.addClientHandlerExtension(this,Client.PROTOCOL_REST,RestHandler.class);
	}
	
	/* (non-Javadoc)
	 * @see net.luis.common.kernel.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.REMOTECLIENT;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void init() {
		ClientHandlerRegistry.instance.init();
		ClientFilterRegistry.instance.init();
		ServiceUrlResolverManager.instance.init();
	}

}
