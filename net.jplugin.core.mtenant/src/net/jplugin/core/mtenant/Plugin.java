package net.jplugin.core.mtenant;

import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.mtenant.impl.MtDataSourceWrapperService;

public class Plugin extends AbstractPlugin{
	
	public Plugin(){
		ExtensionDasHelper.addConnWrapperExtension(this, MtDataSourceWrapperService.class);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.MULTI_TANANT;
	}
	
}
