package net.jplugin.core.rclient;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientFactory;
import net.jplugin.core.rclient.api.IClientHandler;
import net.jplugin.core.rclient.handler.ClientHandlerRegistry;
import net.jplugin.core.rclient.handler.JavaRemotHandler;
import net.jplugin.core.rclient.handler.RestHandler;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-14 上午09:35:53
 **/

public class Plugin extends AbstractPlugin{
	public static final String EP_CLIENT_HANDLER ="EP_CLIENT_HANDLER";
	
	
	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.create(EP_CLIENT_HANDLER, IClientHandler.class, true));
		
		ExtendsionClientHelper.addClientHandlerExtension(this,Client.PROTOCOL_REMOJAVA,JavaRemotHandler.class);
		ExtendsionClientHelper.addClientHandlerExtension(this,Client.PROTOCOL_REST,RestHandler.class);
	}
	
	/* (non-Javadoc)
	 * @see net.luis.common.kernel.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		// TODO Auto-generated method stub
		return CoreServicePriority.REMOTECLIENT;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void init() {
		ClientHandlerRegistry.instance.init();
	}

}
