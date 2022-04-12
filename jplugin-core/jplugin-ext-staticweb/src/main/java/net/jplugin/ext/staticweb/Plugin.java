package net.jplugin.ext.staticweb;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CorePlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;

public class Plugin extends AbstractPlugin{

	@Override
	public void init() {
		
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.STATIC_WEB;
	}

	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}

}
