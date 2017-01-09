package net.jplugin.core.mtenant;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.mtenant.impl.MtDataSourceWrapperService;
import net.jplugin.core.mtenant.impl.filter.MtInvocationFilter;
import net.jplugin.ext.webasic.ExtensionWebHelper;

public class Plugin extends AbstractPlugin{
	
	public Plugin(){
		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable"))){
			ExtensionDasHelper.addConnWrapperExtension(this, MtDataSourceWrapperService.class);
			ExtensionWebHelper.addServiceFilterExtension(this, MtInvocationFilter.class);
			ExtensionWebHelper.addWebCtrlFilterExtension(this, MtInvocationFilter.class);
		}else{
			System.out.println("@@@ mtenant ignore!");
		}
	}
	
	@Override
	public void init() {
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.MULTI_TANANT;
	}
	
}
