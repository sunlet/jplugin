package net.jplugin.core.kernel;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class PluginApp {
	public static void main(String[] args) {
		
		String cfgDir = System.getProperty("plugin-config.path");
		String workDir = System.getProperty("work-dir");
		
		if (StringKit.isNull(cfgDir)){
			System.out.println("Can't find [plugin-config.path] system property. use default value");
			cfgDir = "./config";
		}
		PluginEnvirement.getInstance().setConfigDir(cfgDir);
		if (StringKit.isNull(workDir)){
			System.out.println("Can't find the [work-dir] system property. use default value");
			workDir = "./work";
		}
		PluginEnvirement.getInstance().setWorkDir(workDir);
		PluginEnvirement.getInstance().startup();
	}
}
