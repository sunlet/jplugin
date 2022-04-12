package net.jplugin.core.config.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.LogFactory;

import net.jplugin.common.kits.StringMatcher;
import net.jplugin.core.config.Plugin;
import net.jplugin.core.config.api.ConfigChangeManager;
import net.jplugin.core.config.api.IConfigChangeContext;
import net.jplugin.core.config.api.IConfigChangeHandler;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class ConfigureChangeManager {
	private ConfigChangeHandlerDef[] list;
	
	public static ConfigureChangeManager instance = new ConfigureChangeManager();
	
	public  void init(){
		list = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_CONFIG_CHANGE_HANDLER,ConfigChangeHandlerDef.class);
		
		for (ConfigChangeHandlerDef o:list) {
			o.setMatcher(new StringMatcher(o.pattern));
			o.setHandlerInstance(makeInstance(o.getHandlerClass()));
		}
		
	}

	private IConfigChangeHandler makeInstance(Class<? extends IConfigChangeHandler> handlerClass) {
		IConfigChangeHandler o;
		try {
			o = handlerClass.newInstance();
			PluginEnvirement.getInstance().resolveRefAnnotation(o);
		} catch (Exception e) {
			throw new RuntimeException("init object error:"+handlerClass.getName());
		}
		return o;
	}

	public void fireConfigChange(List<String> keyList){
		PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Fire config change:"+get(keyList));
		for (ConfigChangeHandlerDef en:list){
			//找到匹配的keys，加入到temp
			if (match(en.matcher,keyList)){
				ConfigChangeContext ctx = ConfigChangeContext.create(en.matcher, keyList);
				en.getHandlerInstance().onChange(ctx);
			}
		}
	}
	
	private String get(List<String> keyList) {
		StringBuffer sb = new StringBuffer("[");
		for (String k:keyList) {
			sb.append(k).append(" , ");
		}
		sb.append("]");
		return sb.toString();
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
