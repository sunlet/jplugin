package net.jplugin.core.kernel.api;

import net.jplugin.core.kernel.Plugin;

public class ExtensionKernelHelper {
	public static void addStartUpExtension(AbstractPlugin p,Class startupClazz){
		p.addExtension(Extension.create(net.jplugin.core.kernel.Plugin.EP_STARTUP,"",startupClazz));
	}
	public static void addAnnoAttrHandlerExtension(AbstractPlugin plugin, Class class1) {
		plugin.addExtension(Extension.create(Plugin.EP_ANNO_FOR_ATTR, class1));
	}
	
	public static void addExecutorFilterExtension(AbstractPlugin plugin,Class c){
		plugin.addExtension(Extension.create(Plugin.EP_EXECUTOR_FILTER,c));
	}
}
