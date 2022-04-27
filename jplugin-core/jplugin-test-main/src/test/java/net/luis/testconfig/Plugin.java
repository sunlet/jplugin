package net.luis.testconfig;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.ExtensionObjects;
import net.jplugin.core.kernel.api.PluginAnnotation;

@PluginAnnotation
public class Plugin extends AbstractPlugin {

	@Override
	public void init() {
//		PluginEnvirement.INSTANCE.resolveRefAnnotation(new MyBean());
		
		ExtensionObjects.get("testconfigbean",net.luis.testconfig.impl.TestCfgs.class).test();
	}

	@Override
	public int getPrivority() {
		return 0;
	}

}
