package net.luis.main;

import net.jplugin.core.ctx.api.RuleServiceDefinition;
import net.jplugin.core.event.api.EventAliasDefine;
import net.jplugin.core.event.api.Channel.ChannelType;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.rclient.ExtendsionClientHelper;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.IServiceUrlResolver;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.luis.main.event.EventFilter;
import net.luis.main.event.ITestEventService;
import net.luis.main.event.TestEvent;
import net.luis.main.event.TestEventConsumer;
import net.luis.main.event.TestEventConsumerForFilter;
import net.luis.main.event.TestEventService;
import net.luis.main.pojo.IDataTest;
import net.luis.main.pojo.POProvider;
import net.luis.main.pojo.UserBean;
import net.luis.main.pojo.DataTestImpl;
import net.luis.main.remote.IServerObject;
import net.luis.main.remote.ServerObject;
import net.luis.main.testrule.Rule1;
import net.luis.main.testrule.Rule1Impl;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-3-12 ����06:17:05
 **/




@PluginAnnotation
public class Plugin extends AbstractPlugin{
	public Plugin(){
		addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_RULE_SERVICE, Rule1.class.getName(),RuleServiceDefinition.class,new String[][]{{"interf",Rule1.class.getName()},{"impl",Rule1Impl.class.getName()}} ));
		addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_RULE_SERVICE, IDataTest.class.getName(),RuleServiceDefinition.class,new String[][]{{"interf",IDataTest.class.getName()},{"impl",DataTestImpl.class.getName()}} ));
		addExtension(Extension.create(net.jplugin.core.das.hib.Plugin.EP_DATAMAPPING, "",POProvider.class));


		addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_RULE_SERVICE, ITestEventService.class.getName(),RuleServiceDefinition.class,new String[][]{{"interf",ITestEventService.class.getName()},{"impl",TestEventService.class.getName()}} ));
		addExtension(Extension.createStringExtension(net.jplugin.core.event.Plugin.EP_EVENT_TYPES, TestEvent.TEST_EVENT));
		addExtension(Extension.create(net.jplugin.core.event.Plugin.EP_EVENT_CONSUMER, "",TestEventConsumer.class,new String[][]{{"targetType",TestEvent.TEST_EVENT},{"channelType",ChannelType.POST_MEMORY.toString()}} ));

		addExtension(Extension.create(net.jplugin.core.event.Plugin.EP_EVENT_TYPE_ALIAS, "",EventAliasDefine.class,new String[][]{{"eventType",TestEvent.TEST_EVENT},{"typeAlias",TestEvent.TEST_EVENT_ALIAS},{"filterClass",EventFilter.class.getName()}} ));
		addExtension(Extension.create(net.jplugin.core.event.Plugin.EP_EVENT_CONSUMER, "",TestEventConsumerForFilter.class,new String[][]{{"targetType",TestEvent.TEST_EVENT_ALIAS},{"channelType",ChannelType.POST_MEMORY.toString()}}));
		
		addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, "/testremote", ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass","net.luis.main.remote.ServerObject"}}));

		addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_RULE_SERVICE, "serverobject",RuleServiceDefinition.class,new String[][]{{"interf",IServerObject.class.getName()},{"impl",ServerObject.class.getName()}} ));
		addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, "/testremote2", ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName","serverobject"}}));

		addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, "/userreq", ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",net.luis.main.webreq.UserRequest.class.getName()}}));

		addExtension(Extension.create(net.jplugin.core.kernel.Plugin.EP_STARTUP,"",StartupListener.class));
		
		ExtendsionClientHelper.addServiceUrlResolverExtension(this, Client.PROTOCOL_REST, TestServciceUrlResolver.class);
		
		ExtendsionClientHelper.addClientFilterExtension(this, TestClientFilter.class);
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return 100;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void onCreateServices() {
		
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
