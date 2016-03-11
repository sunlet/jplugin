package net.jplugin.core.config;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;

public class ExtensionConfigHelper {
	public static void addConfigChangeHandlerExtension(AbstractPlugin plugin,Class t){
		plugin.addExtension(Extension.create(Plugin.EP_CONFIG_CHANGE_HANDLER, t));
	}
}
