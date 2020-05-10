package net.jplugin.core.mtenant;

import net.jplugin.core.config.api.ConfigFactory;

public class MtenantStatus {
	private static boolean enable;
	public static void init(){
		enable = "true".equalsIgnoreCase(ConfigFactory.getStringConfigWithTrim("mtenant.enable"));
	}
	public static boolean enabled(){
		return enable;
	}
	

}
