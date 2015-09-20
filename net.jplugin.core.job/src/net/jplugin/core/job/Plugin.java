package net.jplugin.core.job;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;

public class Plugin extends AbstractPlugin{

	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.JOB;
	}

}
