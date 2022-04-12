package net.luis.testfactory;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.ExtensionPointHelper;
import net.jplugin.core.kernel.api.PluginAnnotation;

@PluginAnnotation
public class Plugin extends AbstractPlugin {

	public Plugin() {
		this.addExtensionPoint(ExtensionPoint.createUnique("mypoint78", IMyInterface.class));
	}
	@Override
	public void init() {
		IMyInterface intf = ExtensionPointHelper.getUniqueExtension("mypoint78", IMyInterface.class);
		AssertKit.assertEqual(intf.a(),"thereturn");
	}

	@Override
	public int getPrivority() {
		return 0;
	}

}
