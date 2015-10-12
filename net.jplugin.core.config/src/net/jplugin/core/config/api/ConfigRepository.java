package net.jplugin.core.config.api;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import net.jplugin.common.kits.PropertiesKit;
/**
*
* @author: LiuHang
* @version 创建时间：2015-10-12 下午01:07:22
**/
public class ConfigRepository {
	private static final String POSTFIX = ".config.properties";
	HashMap<String,String> configMap = new HashMap<String,String>();
	
	public String getConfig(String key){
		return configMap.get(key);
	}
	
	public void init(String cfgdir) {
		//load the 
		File[] files = new File(cfgdir).listFiles();
		for (File f:files){
			String fname = f.getName();
			if (fname.endsWith(POSTFIX)){
				String cfgSection = fname.substring(0,fname.length()-POSTFIX.length());
				load(f,cfgSection);
			}
		}
	}

	
	private void load(File f, String cfgSection) {
		Properties prop = PropertiesKit.loadProperties(f.getPath());
		for (Object k:prop.keySet()){
			configMap.put(cfgSection+"."+(String)k, prop.getProperty((String)k));
		}
	}
	
}
