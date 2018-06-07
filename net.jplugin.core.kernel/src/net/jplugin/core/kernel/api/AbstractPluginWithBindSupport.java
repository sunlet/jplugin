package net.jplugin.core.kernel.api;

import java.util.List;

public abstract class AbstractPluginWithBindSupport extends AbstractPlugin {
	public AbstractPluginWithBindSupport() {
		List<IBindExtensionHandler> handlers = AutoBindExtensionManager.INSTANCE.getHandlers();
		for (IBindExtensionHandler h:handlers){
			h.handle(this);
		}
	}
}
