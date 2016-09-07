package net.jplugin.core.kernel.api;

public class ExtensionKernelHelper {
	public static void addStartUpExtension(AbstractPlugin p,Class startupClazz){
		p.addExtension(Extension.create(net.jplugin.core.kernel.Plugin.EP_STARTUP,"",startupClazz));
	}
}
