package net.jplugin.core.log;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午08:44:40
 **/

public class Plugin extends AbstractPlugin{

	public Plugin(){
		this.addExtension(Extension.create(net.jplugin.core.service.api.Constants.EP_SERVICE, ILogService.class.getName(), LogServiceImpl.class));
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
	public void init() {
//		((LogServiceImpl)ServiceFactory.getService(ILogService.class)).init();
	}

}
