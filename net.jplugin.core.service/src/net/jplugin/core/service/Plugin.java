package net.jplugin.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.service.api.Constants;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午09:59:02
 **/

public class Plugin extends AbstractPlugin{

	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.create(Constants.EP_SERVICE,Object.class,true));
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
		ExtensionPoint servicesPoint = PluginEnvirement.getInstance().getExtensionPoint(Constants.EP_SERVICE);
		
		Map<String, Object> map  = servicesPoint.getExtensionMap();
		ServiceFactory.init(map);
	}
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
//	@Override
//	public void init() {
//		ServiceFactory.initAnnotation();
//	}
	
}
