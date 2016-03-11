package net.jplugin.core.config.api;

import java.util.Map;

import net.jplugin.core.config.Plugin;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class ConfigChangeManager {
	private Map<String,IConfigChangeHandler> map;
	
	public static ConfigChangeManager instance = new ConfigChangeManager();
	
	public  void init(){
		map = PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_CONFIG_CHANGE_HANDLER,IConfigChangeHandler.class);
	}
	
	public Map<String, IConfigChangeHandler> getHandlers(){
		return this.map;
	}
}
