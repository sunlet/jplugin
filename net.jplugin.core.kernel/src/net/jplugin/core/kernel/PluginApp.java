package net.jplugin.core.kernel;

import java.io.File;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class PluginApp {
//	private static String mavenProjectConfig="./src/main/resources/config";
//	private static String childConfig="./config";
	
	private static final String JPLUGIN_HOME_PROP="jplugin.home";
	private static final String MAVEN_CONFIG_RELPATH = "/src/main/resources/config";
	private static final String CONFIG_RELPATH = "/application/config";
	private static final String WEB_RELPATH = "/application/web";
	private static final String MAVEN_WEB_RELPATH = "/src/main/webapp";
	

	public static void main(String[] args) {
		if (PluginEnvirement.getInstance().getStarted()){
			return;
		}
		//home目录
		String jpluginHome = System.getProperty(JPLUGIN_HOME_PROP,".");
		jpluginHome = new File(jpluginHome).getAbsolutePath();
		PluginEnvirement.INSTANCE.setWorkDir(jpluginHome);
		PluginEnvirement.INSTANCE.getStartLogger().log("$$$ plugin.home="+PluginEnvirement.INSTANCE.getWorkDir());
		//config目录
		String configDir = jpluginHome +CONFIG_RELPATH;
		if (!FileKit.existsAndIsDir(configDir)){
			configDir = jpluginHome + MAVEN_CONFIG_RELPATH;
		}
		if (!FileKit.existsAndIsDir(configDir)){
			throw new RuntimeException("Can't find the config dir, jplugin.home is "+jpluginHome);
		}
		PluginEnvirement.INSTANCE.setConfigDir(configDir);
		
		//确定web目录,没有的话不会报错
		String webDir = jpluginHome +WEB_RELPATH;
		if (!FileKit.existsAndIsDir(webDir)){
			webDir = jpluginHome + MAVEN_WEB_RELPATH;
		}
		PluginEnvirement.getInstance().setWebRootPath(webDir);
		
		//startup 
		PluginEnvirement.getInstance().startup();
	}

//
//	public static void main(String[] args) {
//		if (PluginEnvirement.getInstance().getStarted()){
//			return;
//		}
//		
////		String path = new File("./src/main/resources/config").getAbsolutePath();
////		System.out.println(path);
////		System.out.println(FileKit.existsFile("./src/main/resources/config"));
////		
//		String cfgDir = System.getProperty("plugin-config.path");
//		String workDir = System.getProperty("work-dir");
//		
//		if (StringKit.isNull(cfgDir)){
//			if (FileKit.existsFile(mavenProjectConfig)){
//				cfgDir = mavenProjectConfig;
//			}else if (FileKit.existsFile(childConfig)){
//				cfgDir = childConfig;
//			}else{
//				throw new RuntimeException("Can't find the config path property: -Dplugin-config.path");
//			}
//		}
//		PluginEnvirement.getInstance().setConfigDir(new File(cfgDir).getAbsolutePath());
//		
//		if (StringKit.isNull(workDir)){
//			System.out.println("Can't find the [work-dir] system property. use current dir");
//			workDir = ".";
//		}
//		PluginEnvirement.getInstance().setWorkDir(new File(workDir).getAbsolutePath());
//		
//		//set web dir
//		PluginEnvirement.getInstance().setWebRootPath(PluginEnvirement.INSTANCE.getWorkDir()+"/web");
//		
//		//startup 
//		PluginEnvirement.getInstance().startup();
//	}
}
