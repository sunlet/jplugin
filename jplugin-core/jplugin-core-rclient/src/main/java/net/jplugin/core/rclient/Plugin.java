package net.jplugin.core.rclient;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientFactory;
import net.jplugin.core.rclient.api.IClientFailHandler;
import net.jplugin.core.rclient.api.IClientFilter;
import net.jplugin.core.rclient.api.IClientHandler;
import net.jplugin.core.rclient.api.IServiceUrlResolver;
import net.jplugin.core.rclient.api.ITokenFetcher;
import net.jplugin.core.rclient.handler.ClientFailHandlerManager;
import net.jplugin.core.rclient.handler.ClientFilterRegistry;
import net.jplugin.core.rclient.handler.ClientHandlerRegistry;
//import net.jplugin.core.rclient.handler.JavaRemotHandler;
import net.jplugin.core.rclient.handler.RestHandler;
import net.jplugin.core.rclient.handler.ServiceUrlResolverManager;
import net.jplugin.core.rclient.proxyfac.ClientProxyDefinition;
import net.jplugin.core.rclient.proxyfac.ClientProxyFactory;
import net.jplugin.core.rclient.proxyfac.TokenFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-14 上午09:35:53
 **/

public class Plugin extends AbstractPlugin{
	public static final String EP_CLIENT_HANDLER ="EP_CLIENT_HANDLER";
	public static final String EP_CLIENT_FILTER ="EP_CLIENT_FILTER";
	public static final String EP_SERVICEURL_RESOLVER = "EP_SERVICEURL_RESOLVER";
	public static final String EP_CLIENT_PROXY = "EP_CLIENT_PROXY";
	public static final String EP_CLIENTFAIL_HANDLER = "EP_CLIENTFAIL_HANDLER";
	public static final String EP_TOKEN_FETCHER = "EP_TOKEN_FETCHER";
	
	
	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.createNamed(EP_CLIENT_HANDLER, IClientHandler.class));
		this.addExtensionPoint(ExtensionPoint.createNamed(EP_SERVICEURL_RESOLVER, IServiceUrlResolver.class));
		this.addExtensionPoint(ExtensionPoint.createList(EP_CLIENT_FILTER, IClientFilter.class));
		this.addExtensionPoint(ExtensionPoint.createNamed(EP_CLIENT_PROXY,ClientProxyDefinition.class));
		//主要用来进行restfule链接失败的通知
		this.addExtensionPoint(ExtensionPoint.createNamed(EP_CLIENTFAIL_HANDLER,IClientFailHandler.class ));

		this.addExtensionPoint(ExtensionPoint.createNamed(EP_TOKEN_FETCHER,ITokenFetcher.class));

//		ExtendsionClientHelper.addClientHandlerExtension(this,Client.PROTOCOL_REMOJAVA,JavaRemotHandler.class);
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
	public void onCreateServices() {
		ClientHandlerRegistry.instance.init();
		ClientFilterRegistry.instance.init();
		ServiceUrlResolverManager.instance.init();
		ClientProxyFactory.instance.init();
		ClientFailHandlerManager.init();
		TokenFactory.init();
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean searchClazzForExtension() {
		return false;
	}

}
