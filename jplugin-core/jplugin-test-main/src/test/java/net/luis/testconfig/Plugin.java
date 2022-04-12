package net.luis.testconfig;

import net.jplugin.common.kits.reso.ResolverKit.Test;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Beans;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.luis.testconfig.impl.MyBean;

@PluginAnnotation
public class Plugin extends AbstractPlugin {

	@Override
	public void init() {
//		PluginEnvirement.INSTANCE.resolveRefAnnotation(new MyBean());
		
		Beans.get("testconfigbean",net.luis.testconfig.impl.TestCfgs.class).test();
	}

	@Override
	public int getPrivority() {
		return 0;
	}

}
