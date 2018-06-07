package net.jplugin.core.kernel.api;

import java.util.ArrayList;
import java.util.List;

public class AutoBindExtensionManager {
	public static AutoBindExtensionManager INSTANCE = new AutoBindExtensionManager();
	
	private List<IBindExtensionHandler> handlers = new ArrayList<IBindExtensionHandler>();
	
	public void addBindExtensionHandler(IBindExtensionHandler h){
		this.handlers.add(h);
	}
	
	public List<IBindExtensionHandler> getHandlers() {
		return handlers;
	}
}
