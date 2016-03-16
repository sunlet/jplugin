package net.jplugin.core.config.api;

import java.util.Map;

public interface IConfigProvidor {
	String getConfigValue(String path);
	boolean containsConfig(String path);
	Map<String, String> getStringConfigInGroup(String group);
}
