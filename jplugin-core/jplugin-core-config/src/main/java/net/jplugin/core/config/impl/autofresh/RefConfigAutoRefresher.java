package net.jplugin.core.config.impl.autofresh;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.PritiveKits;
import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.config.api.RefConfig;
import net.jplugin.core.config.impl.autofresh.RefConfigAutoRefresher.RefreshConfig;
import net.jplugin.core.kernel.api.PluginEnvirement;


public class RefConfigAutoRefresher {
	public static RefConfigAutoRefresher instance = new RefConfigAutoRefresher();

	static Map<String,List<RefreshConfig>> refreshMap=new HashMap<String, List<RefConfigAutoRefresher.RefreshConfig>>();
	
	public  synchronized  void handleAutoFresh(Object theObject, Field f, RefConfig anno) {
		if (!anno.autoRefresh())
			return;
		
		//如果是启动完成以后，报错
		if (PluginEnvirement.getInstance().getStateLevel()>=PluginEnvirement.STAT_LEVEL_WORKING) {
			throw new RuntimeException("AutoFresh for RefConfig can only work when initing! Can't work when state is working! path is :"+ anno.path());
		}
		
		String path = anno.path();
		String groupName = getGroupName(path);
		
		
		//GET LIST
		List<RefreshConfig> list = refreshMap.get(groupName);
		if (list==null) {
			//create list
			list = new ArrayList(2);
			refreshMap.put(groupName, list);	
		}else {
			//check list
			check(list,f,theObject,anno);
		}
		
		//ADD TO LIST
		RefreshConfig raf = new RefreshConfig(theObject,f,anno);
		list.add(raf);
	}
	
	private String getGroupName(String path) {
		int pos = path.indexOf(".");
		if (pos<0) {
			return path;
		}else {
			return path.substring(0,pos);
		}
	}

	//一个路径下面，一个属性只能有一个配置
	private  void check(List<RefreshConfig> list,Field f, Object theObject, RefConfig anno) {
		for (RefreshConfig o:list) {
			if (o.field.equals(f)) {
				throw new RuntimeException("Auto refresh can only be used in one class. path:"+anno.path()+" class:"+theObject.getClass().getName());
			}
		}
	}
	
	
	static class RefreshConfig {
		public RefreshConfig(Object o, Field f, RefConfig a) {
			this.object = o;
			this.field = f;
			this.anno = a;
		}
		Field field;
		RefConfig anno;
		Object object;
	}


	public synchronized  void keysChanged(List<String> keys) {
		for (String key:keys) {
			//因为目前变更传入的是组的名字，不包含具体的key。所以用path
			if (refreshMap.containsKey(key)) {
				refresh(refreshMap.get(key));
			}
		}
	}

	private  void refresh(List<RefreshConfig> list) {
		//逐个更新
		for (RefreshConfig rc:list) {
			String val = ConfigFactory.getStringConfig(rc.anno.path(),rc.anno.defaultValue());
			Class<?> ft = rc.field.getType();
			Object attrValue = PritiveKits.getTransformer(ft).fromString(ft,val);
			ReflactKit.setFieldValueForce(rc.field, rc.object, attrValue);
			PluginEnvirement.getInstance().getStartLogger().log("##Config auto changed for path:"+rc.anno.path()+" object:"+rc.object+" new val:"+attrValue);
		}
	}

}
