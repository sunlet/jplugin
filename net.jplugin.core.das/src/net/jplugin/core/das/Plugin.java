package net.jplugin.core.das;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;

public class Plugin extends AbstractPlugin {
	public Plugin(){
		
	}


	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS;
	}
	
	public void init() {
	}

}
