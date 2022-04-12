package net.luis.testautosearch.child;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;

@PluginAnnotation
public class Plugin extends AbstractPlugin {
	public Plugin() {
		this.addExtensionPoint(ExtensionPoint.create("EL_TESTPOING100", ITestExten.class));
	}
	
	@Override
	public void init() {
	}

	@Override
	public int getPrivority() {
		return 1000;
	}

}
