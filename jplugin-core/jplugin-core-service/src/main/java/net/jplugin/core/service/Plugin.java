package net.jplugin.core.service;

import net.jplugin.core.kernel.api.*;
import net.jplugin.core.kernel.kits.ExtensionBindKit;
import net.jplugin.core.service.api.BindExportService;
import net.jplugin.core.service.api.BindServiceExport;
import net.jplugin.core.service.api.Constants;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.core.service.impl.ServiceAttrAnnoHandler;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午09:59:02
 **/

public class Plugin extends AbstractPlugin{

	public static final String EP_SERVICE = "EP_SERVICE";
//	public static final String EP_SERVICE_EXPORT = "EP_RESTMETHOD";
	public static final String EP_SERVICE_EXPORT = "EP_SERVICE_EXPORT";

	static{
		AutoBindExtensionManager.INSTANCE.addBindExtensionHandler((p)->{
			ExtensionServiceHelper.autoBindServiceExtension(p, "");
		});

		AutoBindExtensionManager.INSTANCE.addBindExtensionTransformer(BindExportService.class, (plugin, clazz, a)->{
			BindExportService anno = (BindExportService) a;
			ExtensionServiceHelper.addExportServiceExtension(plugin, anno.path(), clazz);

			ExtensionBindKit.handleIdAndPriority(plugin,clazz);
		});
		AutoBindExtensionManager.INSTANCE.addBindExtensionTransformer(BindServiceExport.class, (plugin, clazz, a)->{
			BindServiceExport anno = (BindServiceExport) a;
			ExtensionServiceHelper.addExportServiceExtension(plugin, anno.path(), clazz);

			ExtensionBindKit.handleIdAndPriority(plugin,clazz);
		});

	}
	
	public Plugin(){
//		this.addExtensionPoint(ExtensionPoint.createNamed(EP_SERVICE,Object.class));
		this.addExtensionPoint(ExtensionPoint.createList(EP_SERVICE,Object.class));
		this.addExtensionPoint(ExtensionPoint.createNamed(EP_SERVICE_EXPORT,Object.class));
		ExtensionKernelHelper.addAnnoAttrHandlerExtension(this, ServiceAttrAnnoHandler.class);
	}
	/* (non-Javadoc)
	 * @see net.luis.common.kernel.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.SERVICE;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void onCreateServices() {
//		ServiceFactory.init(PluginEnvirement.getInstance().getExtensionMap(Constants.EP_SERVICE));
		ServiceFactory.initExtensions(PluginEnvirement.getInstance().getExtensionList(EP_SERVICE));
	}
	public void init() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}
	
//	@Override
//	public void init() {
//		ServiceFactory.initAnnotation();
//	}
	
}
