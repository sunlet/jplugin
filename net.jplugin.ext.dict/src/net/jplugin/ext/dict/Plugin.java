package net.jplugin.ext.dict;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.service.ExtensionServiceHelper;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.dict.api.ExtensionDictHelper;
import net.jplugin.ext.dict.api.IDictProvidor;
import net.jplugin.ext.dict.api.IDictionaryService;
import net.jplugin.ext.dict.impl.DictionaryServiceImpl;
import net.jplugin.ext.dict.test.TestDictProvider;
import net.jplugin.ext.webasic.ExtensionWebHelper;

public class Plugin extends AbstractPlugin{
	public static final String EP_DICT_PROVIDER = "EP_DICT_PROVIDER";

	public Plugin() {
		this.addExtensionPoint(ExtensionPoint.create(EP_DICT_PROVIDER, IDictProvidor.class,true));
		ExtensionServiceHelper.addServiceExtension(this, IDictionaryService.class.getName(), DictionaryServiceImpl.class);
		ExtensionDictHelper.addDictProvdExtension(this, "testdict", TestDictProvider.class);
		ExtensionWebHelper.addWebExControllerExtension(this, "/sys/dict", DictController.class);
	}
	
	@Override
	public void onCreateServices() {
		DictionaryServiceImpl svc = (DictionaryServiceImpl) ServiceFactory.getService(IDictionaryService.class);
		svc.init(PluginEnvirement.getInstance().getExtensionMap(EP_DICT_PROVIDER,IDictProvidor.class));
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DICT;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
