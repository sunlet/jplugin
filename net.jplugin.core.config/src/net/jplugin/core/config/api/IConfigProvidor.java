package net.jplugin.core.config.api;

import java.util.Map;
import java.util.Set;

public interface IConfigProvidor {
	String getConfigValue(String path);
	boolean containsConfig(String path);
	Map<String, String> getStringConfigInGroup(String group);
	Set<String> getGroups();
}
