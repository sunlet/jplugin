package net.jplugin.core.kernel.api.plugin_event;

public interface IPluginEventListener {
	public String getIntrestedPluginName();
	
	public void afterCreateServices(String pluginName);
}
