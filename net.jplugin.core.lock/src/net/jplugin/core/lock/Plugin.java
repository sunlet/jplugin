package net.jplugin.core.lock;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;

public class Plugin extends AbstractPlugin{

	public void init() {
		
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.LOCK;
	}

}
