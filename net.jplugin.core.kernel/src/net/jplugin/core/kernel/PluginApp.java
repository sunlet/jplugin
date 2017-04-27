package net.jplugin.core.kernel;

import java.io.File;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class PluginApp {
	private static String mavenProjectConfig="./src/main/resources/config";
	private static String childConfig="./config";

	public static void main(String[] args) {
		if (PluginEnvirement.getInstance().getStarted()){
			return;
		}
		
		String path = new File("./src/main/resources/config").getAbsolutePath();
		System.out.println(path);
		System.out.println(FileKit.existsFile("./src/main/resources/config"));
		
		String cfgDir = System.getProperty("plugin-config.path");
		String workDir = System.getProperty("work-dir");
		
		if (StringKit.isNull(cfgDir)){
			if (FileKit.existsFile(mavenProjectConfig)){
				cfgDir = mavenProjectConfig;
			}else if (FileKit.existsFile(childConfig)){
				cfgDir = childConfig;
			}else{
				throw new RuntimeException("Can't find the config path property: -Dplugin-config.path");
			}
		}
		PluginEnvirement.getInstance().setConfigDir(new File(cfgDir).getAbsolutePath());
		
		if (StringKit.isNull(workDir)){
			System.out.println("Can't find the [work-dir] system property. use current dir");
			workDir = ".";
		}
		PluginEnvirement.getInstance().setWorkDir(new File(workDir).getAbsolutePath());
		PluginEnvirement.getInstance().startup();
	}
}
