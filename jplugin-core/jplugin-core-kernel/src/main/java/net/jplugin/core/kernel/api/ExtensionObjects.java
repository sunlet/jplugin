package net.jplugin.core.kernel.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jplugin.common.kits.StringKit;

public class ExtensionObjects {
	static HashMap<String,Object> initiateMap=new HashMap<String, Object>();
	static HashMap<Object,Object> resetingMap=new HashMap<Object, Object>();

	/**
	 * 通过Extension的ID属性获取扩展点对象，如果找不到会抛出异常。同时转化为泛型T
	 * @param id
	 * @param t
	 * @param <T>
	 * @return
	 */
	public static <T> T get(String id,Class<T> t) {
		return (T) get(id);
	}

	/**
	 * 通过Extension的ID属性获取扩展点对象，如果找不到会抛出异常
	 * @param id
	 * @return
	 */
	public static Object get(String id) {
		Object o = find(id);
		if (o!=null) return o;
		else throw new RuntimeException("Extension not found by id:"+id);
	}

	/**
	 * 通过Extension的ID属性获取扩展点对象，如果找不到返回null
	 * @param id
	 * @return
	 */
	public static Object find(String id) {
		if (PluginEnvirement.INSTANCE.getStateLevel()<PluginEnvirement.STAT_LEVEL_RESOLVING_HIST)
			throw new RuntimeException("Can't call when state is before STAT_LEVEL_RESOLVING_HIST");
		
		Object val = initiateMap.get(id);
		if (val==null) {
			return null;
		}
		Object resetVal = resetingMap.get(val);
		
		//优先返回resetVal,空时才返回 val
		if (resetVal!=null) {
			return resetVal;
		}else {
			return val;
		}
	}
	
	/**
	 * <PRE>
	 * id一定是已经存在的，并且只能在disabledModify=true情况下。
	 * 注意：
	 * 目前的情况下，可以保证同一个ID指向的对象肯定是只有一个。
	 * 但不不同的ID可能指向同一个对象。比如对于 RuleInterceptor 和 WebController的Extension.
	 * </PRE>
	 * @param id
	 * @param value
	 */
	public static void resetValue(Object key,Object value) {
		if (PluginEnvirement.INSTANCE.getStateLevel()>=PluginEnvirement.STAT_LEVEL_INITING)
			throw new RuntimeException("Can't be called after Modify STAT_LEVEL_INITING!");

		
		//找不到对应的value，则不会调用
		if (initiateMap.containsValue(key)) {
			resetingMap.put(key, value);
		}
	}
	


	/**
	 * 在load以后被调用，这时所有的ExtensionObject都创建好了。
	 * ExtensionObject必须是new出来不重复对象才可以，所以：如果是extensionObject是Class或者String，则不能有ID！
	 */
	static void initFromPluginList() { 
		if (PluginEnvirement.INSTANCE.getStateLevel()>=PluginEnvirement.STAT_LEVEL_INITING)
			throw new RuntimeException("Can't be called after Modify STAT_LEVEL_INITING!");
		
		Set<String> dupCheckSet = new HashSet<String>();
		
		List<AbstractPlugin> list = PluginEnvirement.getInstance().getPluginRegistry().getPluginList();
		for (AbstractPlugin plugin:list) {
			List<Extension> exts = plugin.getExtensions();
			for (Extension ext:exts) {
				Object extObject = ext.getObject();
				/**
				 * 一定要把Class和String过滤掉，因为这些ExtensionObject不是new出来的对象。可能出现不同的Key指向相同的ExtensionObject。这样在reset的时候，同一个Key可能被调用多次。这样key指向的结果就不可预料了！
				 */
				if (extObject instanceof Class || extObject instanceof String) {
					if (StringKit.isNotNull(ext.getId())) {
						throw new RuntimeException("Extension with type Class or String can't have id: Plugin:"+plugin.getName()+" RefExtensionPoint:"+ext.getExtensionPointName());
					}
				}else {
					String extid = ext.getId();
					if (StringKit.isNotNull(extid)) {
						if (dupCheckSet.contains(extid)){
							throw new RuntimeException("Extension ID is duplicated. Plugin:"+plugin.getName()+" RefExtensionPoint:"+ext.getExtensionPointName()+" id="+extid);
						}else {
							dupCheckSet.add(extid);
						}
						initiateMap.put(extid, ext.getObject());
					}
				}
			}
		}
	}
	
	
	//以下为 extension id相关的维护方法
	
//	static Extension lastAddedExtension;
//	static void setLastExtension(Extension e) {
//		lastAddedExtension = e;
//	}
//	public static void setLastId(String id) {
//		//设置上一次调用addExtension的extension的ID
//		if (lastAddedExtension!=null) {
//			lastAddedExtension.setId(id);
//		}else {
//			throw new RuntimeException("Last extension is null.");
//		}
//	}
//	public static String getLastId() {
//		if (lastAddedExtension!=null) {
//			return lastAddedExtension.getId();
//		}else {
//			throw new RuntimeException("Last extension is null.");
//		}
//	}
}
