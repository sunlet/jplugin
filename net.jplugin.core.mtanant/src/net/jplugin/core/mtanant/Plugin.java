package net.jplugin.core.mtanant;

import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.mtanant.impl.MtDataSourceWrapperService;

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
