package net.jplugin.core.config.impl;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.jplugin.common.kits.PropertiesKit;
import net.jplugin.core.config.api.IConfigProvidor;
/**
*
* @author: LiuHang
* @version 创建时间：2015-10-12 下午01:07:22
**/
public class ConfigRepository implements IConfigProvidor{
	private static final String POSTFIX = ".config.properties";
	HashMap<String,String> configMap = new HashMap<String,String>();
	Set<String> groups = new HashSet<String>();
	
	@Override
	public String getConfigValue(String key){
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
				groups.add(cfgSection);
			}
		}
	}

	
	private void load(File f, String cfgSection) {
		Properties prop = PropertiesKit.loadProperties(f.getPath());
		for (Object k:prop.keySet()){
			configMap.put(cfgSection+"."+(String)k, prop.getProperty((String)k));
		}
	}



	@Override
	public boolean containsConfig(String path) {
		return configMap.containsKey(path);
	}

	@Override
	public Map<String, String> getStringConfigInGroup(String group) {
		String prefix = group+".";
		HashMap<String, String> ret = new HashMap<String,String>();
		for (String k:configMap.keySet()){
			if (k.startsWith(prefix)){
				ret.put(k.substring(group.length()+1), configMap.get(k));
			}
		}
		return ret;
	}

	@Override
	public Set<String> getGroups() {
		return groups;
	}
	
}
