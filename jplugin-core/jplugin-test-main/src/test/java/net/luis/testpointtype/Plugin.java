package net.luis.testpointtype;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.ExtensionPointHelper;
import net.jplugin.core.kernel.api.PluginAnnotation;

@PluginAnnotation
public class Plugin extends AbstractPlugin {

	public Plugin() {
		this.addExtensionPoint(ExtensionPoint.createUnique("testpointtype", String.class));

		this.addExtension(Extension.createStringExtension("testpointtype", "hello"));
	
//		this.addExtension(Extension.createStringExtension("testpointtype", "hello222"));
		
	}

	@Override
	public void init() {
		String ext = ExtensionPointHelper.getExtension("testpointtype",String.class);
		AssertKit.assertEqual(ext, "hello");
	}

	@Override
	public int getPrivority() {
		return 0;
	}

}
