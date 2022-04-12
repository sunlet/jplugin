package net.jplugin.core.log;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionKernelHelper;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.impl.LogAnnoAttrHandler;
import net.jplugin.core.log.impl.LogServiceImpl;
import net.jplugin.core.log.impl.LogServiceImpl4Compatible;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午08:44:40
 **/

@PluginAnnotation(prepareSeq=-2)
public class Plugin extends AbstractPlugin{

	public static void prepare(){
		LogFactory.init();
	}
	
	public Plugin(){
		this.addExtension(Extension.create(net.jplugin.core.service.api.Constants.EP_SERVICE, ILogService.class.getName(), LogServiceImpl4Compatible.class));
		ExtensionKernelHelper.addAnnoAttrHandlerExtension(this, LogAnnoAttrHandler.class);
	}
	
	/* (non-Javadoc)
	 * @see net.luis.common.kernel.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.LOG;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.IPlugin#init()
	 */
	public void onCreateServices() {
		PluginEnvirement.getInstance().getStartLogger().log("Now to create common logging service.....");
		LogFactory.initCommonLoggerService();
	}

	public void init() {
	}

	@Override
	public boolean searchClazzForExtension() {
		return false;
	}

}
