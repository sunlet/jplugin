package net.jplugin.core.config.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jplugin.common.kits.StringMatcher;
import net.jplugin.core.config.Plugin;
import net.jplugin.core.config.api.ConfigChangeManager;
import net.jplugin.core.config.api.IConfigChangeContext;
import net.jplugin.core.config.api.IConfigChangeHandler;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class ConfigureChangeManager {
	private Map<String,IConfigChangeHandler> map;
	private Map<String,StringMatcher> matcherMap;
	
	public static ConfigureChangeManager instance = new ConfigureChangeManager();
	
	public  void init(){
		map = PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_CONFIG_CHANGE_HANDLER,IConfigChangeHandler.class);
		matcherMap = createMatcherMap(map.keySet());
	}
	
	private Map<String, StringMatcher> createMatcherMap(Set<String> keys) {
		Map<String,StringMatcher> m = new HashMap<>();
		for (String k:keys){
			StringMatcher matcher = new StringMatcher(k);
			m.put(k,matcher);
		}
		return m;
	}

	public void fireConfigChange(List<String> keyList){
		for (Entry<String, StringMatcher> en:matcherMap.entrySet()){
			//找到匹配的keys，加入到temp
			if (match(en.getValue(),keyList)){
				ConfigChangeContext ctx = ConfigChangeContext.create(en.getValue(), keyList);
				map.get(en.getKey()).onChange(ctx);
			}
		}
	}
	
	private boolean match(StringMatcher m, List<String> keyList) {
		for (String key:keyList){
			if (m.match(key)){
				return true;
			}
		}
		return false;
	}
	
	
	private static class ConfigChangeContext implements IConfigChangeContext{
		StringMatcher matcher;
		List<String> keys;
		
		static ConfigChangeContext create(StringMatcher matcher,List<String> keys){
			ConfigChangeContext o= new ConfigChangeContext();
			o.matcher = matcher;
			o.keys = keys;
			return o;
		}
		
		@Override
		public List<String> getChangedKeys() {
			
			List<String> result = new ArrayList();
			for (String key:keys){
				if (matcher.match(key)){
					result.add(key);
				}
			}
			
			if (result.isEmpty()){
				throw new RuntimeException("No change key found,code shoudln't come here!");
			}
			return result;
		}
	}
}
