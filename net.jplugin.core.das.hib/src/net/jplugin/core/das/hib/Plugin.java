package net.jplugin.core.das.hib;

import net.jplugin.core.das.hib.api.IDataService;
import net.jplugin.core.das.hib.api.IMtDataService;
import net.jplugin.core.das.hib.api.IPersistObjDefinition;
import net.jplugin.core.das.hib.api.SinglePoDefine;
import net.jplugin.core.das.hib.impl.DataService4Hibernate;
import net.jplugin.core.das.hib.mt.MtDataService;
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
 * @version 创建时间：2015-2-9 上午08:35:48
 **/

public class Plugin extends AbstractPlugin{

	public static final String EP_DATAMAPPING = "EP_DATAMAPPING";
	public static final String EP_DATAMAPPING_SINGLE = "EP_DATAMAPPING_SINGLE";

	public Plugin(){
		if (noHib()){
//			System.out.println("Hiberinate env not found,skipped!");
			return;
		}
		addExtensionPoint(ExtensionPoint.create(EP_DATAMAPPING, IPersistObjDefinition.class));
		addExtensionPoint(ExtensionPoint.create(EP_DATAMAPPING_SINGLE, SinglePoDefine.class));

		addExtension(Extension.create(Constants.EP_SERVICE, IDataService.class.getName(),DataService4Hibernate.class));
		addExtension(Extension.create(Constants.EP_SERVICE, IMtDataService.class.getName(),MtDataService.class));
	}
	
	public static boolean noHib() {
		try {
			Class.forName("org.hibernate.SessionFactory");
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	/* (non-Javadoc)
	 * @see net.luis.common.kernel.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_HIB;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void init() {
		if (noHib()){
			System.out.println("Hiberinate env not found,not init!");
			return;
		}
		
		IPersistObjDefinition[] podefs = (IPersistObjDefinition[]) PluginEnvirement.getInstance().getExtensionObjects(EP_DATAMAPPING);
		SinglePoDefine[] singlePoDefs = (SinglePoDefine[]) PluginEnvirement.getInstance().getExtensionObjects(EP_DATAMAPPING_SINGLE);

		
		DataService4Hibernate das = ((DataService4Hibernate)ServiceFactory.getService(IDataService.class));
		das.init(podefs,singlePoDefs);
		
		MtDataService mtsvc = (MtDataService) ServiceFactory.getService(IMtDataService.class);
		mtsvc.initDataService(ServiceFactory.getService(IDataService.class));
	}

}
