package net.jplugin.core.event;


import net.jplugin.core.event.api.Channel;
import net.jplugin.core.event.api.EventAliasDefine;
import net.jplugin.core.event.api.EventConsumer;
import net.jplugin.core.event.impl.ChannelFacade;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.service.api.Constants;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午03:20:27
 **/

public class Plugin extends AbstractPlugin{
	
	public static final String EP_EVENT_TYPES = "EP_EVENT_TYPES";
	public static final String EP_EVENT_TYPE_ALIAS = "EP_EVENT_TYPE_ALIAS";
	
	public static final String EP_EVENT_CONSUMER = "EP_EVENT_CONSUMER";

	public Plugin(){
		addExtensionPoint(ExtensionPoint.create(EP_EVENT_TYPES, String.class));
		addExtensionPoint(ExtensionPoint.create(EP_EVENT_CONSUMER, EventConsumer.class));
		addExtensionPoint(ExtensionPoint.create(EP_EVENT_TYPE_ALIAS, EventAliasDefine.class));
		
		addExtension(Extension.create(Constants.EP_SERVICE, Channel.class.getName(), ChannelFacade.class));
	}
	@Override
	public int getPrivority() {
		return CoreServicePriority.EVENT;
	}

	public void onCreateServices() {
		String[] eventTypes = PluginEnvirement.getInstance().getExtensionObjects(EP_EVENT_TYPES,String.class);
		EventAliasDefine[] typeAliases = PluginEnvirement.getInstance().getExtensionObjects(EP_EVENT_TYPE_ALIAS,EventAliasDefine.class);
		EventConsumer[] eventConsumers = PluginEnvirement.getInstance().getExtensionObjects(EP_EVENT_CONSUMER,EventConsumer.class);
		
		ChannelFacade channel = (ChannelFacade) ServiceFactory.getService(Channel.class.getName(),Channel.class);
		channel.init(eventTypes, typeAliases,eventConsumers);
	}
	public void init() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}

}
