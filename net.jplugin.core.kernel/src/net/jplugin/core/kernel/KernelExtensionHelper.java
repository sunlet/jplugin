package net.jplugin.core.kernel;

import net.jplugin.core.kernel.api.Extension;

public class KernelExtensionHelper {

	public static void addAnnoAttrHandlerExtension(Plugin plugin, Class class1) {
		plugin.addExtension(Extension.create(Plugin.EP_ANNO_FOR_ATTR, class1));
	}

}
