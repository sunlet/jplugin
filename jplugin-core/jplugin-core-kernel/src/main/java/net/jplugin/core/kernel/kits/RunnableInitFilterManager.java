package net.jplugin.core.kernel.kits;

import net.jplugin.core.kernel.api.PluginFilterManager;

public class RunnableInitFilterManager {
	static PluginFilterManager<RunnableWrapper> filterManager = new PluginFilterManager<>(net.jplugin.core.kernel.Plugin.EP_EXE_RUN_INIT_FILTER,null);
	
	public static void init(){
		filterManager.init();
	}
	public static void filter(RunnableWrapper o){
		filterManager.filter(o);
	}
}
