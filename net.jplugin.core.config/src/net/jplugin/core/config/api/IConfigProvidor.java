package net.jplugin.core.config.api;

public interface IConfigProvidor {
	String getConfigValue(String path);
	boolean containsConfig(String path);
}
