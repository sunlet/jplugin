package net.jplugin.core.config;

import net.jplugin.core.config.impl.ConfigChangeHandlerDef;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;

public class ExtensionConfigHelper {
	public static void addConfigChangeHandlerExtension(AbstractPlugin plugin,String keyformat,Class t){
//		plugin.addExtension(Extension.create(Plugin.EP_CONFIG_CHANGE_HANDLER,keyformat,t));
		plugin.addExtension(Extension.create(Plugin.EP_CONFIG_CHANGE_HANDLER,ConfigChangeHandlerDef.class,new String[][] {{"pattern",keyformat},{"handlerClass",t.getName()}}));
	}
}
