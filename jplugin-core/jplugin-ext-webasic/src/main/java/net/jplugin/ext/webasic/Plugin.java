package net.jplugin.ext.webasic;

import net.jplugin.common.kits.http.ContentKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.*;
import net.jplugin.core.kernel.kits.ExtensionBindKit;
import net.jplugin.core.service.ExtensionServiceHelper;
import net.jplugin.ext.webasic.api.*;
import net.jplugin.ext.webasic.api.esf.IESFRestFilter;
import net.jplugin.ext.webasic.api.esf.IESFRpcFilter;
import net.jplugin.ext.webasic.impl.*;
import net.jplugin.ext.webasic.impl.filter.service.ServiceFilterManager;
import net.jplugin.ext.webasic.impl.filter.webctrl.WebCtrlFilterManager;
import net.jplugin.ext.webasic.impl.restm.RestMethodControllerSet4Invoker;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvoker;
import net.jplugin.ext.webasic.impl.web.WebControllerSet;
import net.jplugin.ext.webasic.impl.web.webex.WebExControllerSet;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-2 下午04:57:02
 **/

public class Plugin extends AbstractPlugin{

	public static final String EP_WEBFILTER = "EP_WEBFILTER";
	public static final String EP_CONTROLLERSET = "EP_CONTROLLERSET";

	public static final String EP_WEBCONTROLLER = "EP_WEBCONTROLLER";
	public static final String EP_WEBEXCONTROLLER = "EP_WEBEXCONTROLLER";
	public static final String EP_RESTMETHOD = "EP_RESTMETHOD";
//	public static final String EP_RESTSERVICE = "EP_RESTSERVICE";
//	public static final String EP_REMOTECALL = "EP_REMOTECALL";
	
	public static final String EP_SERVICEFILTER = "EP_SERVICEFILTER";
	public static final String EP_WEBCTRLFILTER = "EP_WEBCTRLFILTER";
	public static final String EP_HTTP_FILTER = "EP_HTTP_FILTER";
	public static final String EP_ESF_RPC_FILTER = "EP_ESF_RPC_FILTER";
	public static final String EP_ESF_REST_FILTER = "EP_ESF_REST_FILTER";

	static{
//		AutoBindExtensionManager.INSTANCE.addBindExtensionHandler((p)->{
////			ExtensionWebHelper.autoBindControllerExtension(p, "");
////			ExtensionWebHelper.autoBindServiceExportExtension(p, "");
//		});

		AutoBindExtensionManager.INSTANCE.addBindExtensionTransformer(BindController.class, (plugin, clazz, a)->{
			BindController anno = (BindController) a;
			if (AbstractExController.class.isAssignableFrom(clazz)) {
				ExtensionWebHelper.addWebExControllerExtension(plugin, anno.path(), clazz);
				ExtensionBindKit.handleIdAndPriority(plugin,clazz);
			} else {
				ExtensionWebHelper.addWebControllerExtension(plugin, anno.path(), clazz);
				ExtensionBindKit.handleIdAndPriority(plugin,clazz);
			}

			ExtensionBindKit.handleIdAndPriority(plugin,clazz);
		});


		AutoBindExtensionManager.INSTANCE.addBindExtensionTransformer(BindServiceExport.class, (plugin, clazz, a)->{
			BindServiceExport anno = (BindServiceExport) a;
			ExtensionServiceHelper.addServiceExportExtension(plugin, anno.path(), clazz);

			ExtensionBindKit.handleIdAndPriority(plugin,clazz);
		});
	}
	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.create(EP_CONTROLLERSET, IControllerSet.class));
		this.addExtensionPoint(ExtensionPoint.create(EP_WEBFILTER, WebFilter.class));
//		this.addExtensionPoint(ExtensionPoint.create(EP_RESTSERVICE, ObjectDefine.class, true));
//		this.addExtensionPoint(ExtensionPoint.create(EP_WEBCONTROLLER, ObjectDefine.class, true));
//		this.addExtensionPoint(ExtensionPoint.create(EP_WEBEXCONTROLLER, ClassDefine.class, true));
		this.addExtensionPoint(ExtensionPoint.create(EP_WEBCONTROLLER, Object.class, true));
		this.addExtensionPoint(ExtensionPoint.create(EP_WEBEXCONTROLLER, Object.class, true));
//		this.addExtensionPoint(ExtensionPoint.create(EP_REMOTECALL, Object.class, true));
//		this.addExtensionPoint(ExtensionPoint.create(EP_RESTMETHOD, Object.class, true));
		this.addExtensionPoint(ExtensionPoint.create(EP_SERVICEFILTER, IInvocationFilter.class,false));
		this.addExtensionPoint(ExtensionPoint.create(EP_WEBCTRLFILTER, IInvocationFilter.class,false));
		this.addExtensionPoint(ExtensionPoint.create(EP_HTTP_FILTER, IHttpFilter.class,false));
		this.addExtensionPoint(ExtensionPoint.create(EP_ESF_RPC_FILTER, IESFRpcFilter.class,false));
		this.addExtensionPoint(ExtensionPoint.create(EP_ESF_REST_FILTER, IESFRestFilter.class,false));
		
		this.addExtension(Extension.create(EP_WEBFILTER,"",InitRequestInfoFilter.class));
		this.addExtension(Extension.create(EP_WEBFILTER,"",InitRequestInfoFilterNew.class));
		
		this.addExtension(Extension.create(EP_CONTROLLERSET,"",WebControllerSet.class));
//		this.addExtension(Extension.create(EP_CONTROLLERSET,"",ServiceControllerSet.class));
//		this.addExtension(Extension.create(EP_CONTROLLERSET,"",RmethodControllerSet.class));
//		this.addExtension(Extension.create(EP_CONTROLLERSET,"",RestMethodControllerSet.class));
//		this.addExtension(Extension.create(EP_CONTROLLERSET,"",RmethodControllerSet4Invoker.class));
		this.addExtension(Extension.create(EP_CONTROLLERSET,"",RestMethodControllerSet4Invoker.class));
		this.addExtension(Extension.create(EP_CONTROLLERSET,"",WebExControllerSet.class));
	}


	/* (non-Javadoc)
	 * @see net.luis.common.kernel.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.WEBSERVICE;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void onCreateServices() {
		WebDriver.INSTANCE.init();
		ServiceFilterManager.INSTANCE.init();
		WebCtrlFilterManager.INSTANCE.init();
		
		ServiceInvoker.initCompatibleReturn();
//		HttpFilterManager.addFilter(new HttpRequestIdChain());
		
		MtInvocationFilterHandler.init();
		ESFHelper.init();
		
		//初始化一下兼容设置
		//1.7.0 默认不再兼容旧的application/json检查。不能在代码当中直接读取流了。
		ContentKit.init(Boolean.parseBoolean(ConfigFactory.getStringConfig("platform.json-check-compatible","false")));
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
