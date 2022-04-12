package net.jplugin.core.kernel.api.plugin_event;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.kernel.api.PluginEnvirement;

public class PluginEventListenerManager {
	//因为这种场景很小,省事,不再设计Map<List>结构了!
	static List<IPluginEventListener> list=null;
	
	public static void addListener(IPluginEventListener listener) {
		if (list==null) { 
			list = new ArrayList<IPluginEventListener>();
		}
		
		list.add(listener);
	}
	
	public static void afterCreateServices(String pluginName) {
		if (list == null)
			return;
		for (IPluginEventListener l:list) {
			if (pluginName.equals(l.getIntrestedPluginName())) {
				PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Now Trigger PluginEventListener for "+pluginName);
				l.afterCreateServices(pluginName);
			}
		}
	}
	
}
