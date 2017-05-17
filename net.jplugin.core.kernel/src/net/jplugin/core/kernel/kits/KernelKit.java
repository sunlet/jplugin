package net.jplugin.core.kernel.kits;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class KernelKit {
	private static final String DEFAULT_ENV = "develop";

	public static String getConfigFilePath(String filename){
		//先找不带env的缺省文件
		String path = getFileName("",filename);
		if (FileKit.existsAndIsFile(path)){
			return path;
		}
		//找env文件，如果没有配置则找缺省env
		String envtype = PluginEnvirement.INSTANCE.getEnvType();
		if (envtype==null){
			envtype=DEFAULT_ENV;
		}
		path = getFileName(envtype,filename);
		if (FileKit.existsAndIsFile(path)){
			return path;
		}else{
			//通过各种渠道找不到文件，也找不到develop，使用默认值。应该找不到文件会报错的。
			return getFileName("",filename);
		}
	}
	private static String getFileName(String env, String filename) {
		if (StringKit.isNull(env)) 
			return PluginEnvirement.getInstance().getConfigDir()+"/"+filename;
		else
			return PluginEnvirement.getInstance().getConfigDir()+"/"+env+"/"+filename;
	}
	
	private static String getEnvType(){
		return System.getProperty("plugin.env");
	}
}
