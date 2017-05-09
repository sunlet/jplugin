package net.jplugin.core.service.api;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午09:55:34
 **/

public class ServiceFactory {
	static Hashtable<String, Object> serviceMap = new Hashtable<String, Object>();
	
	public static <T> T getService(String name,Class<T> clazz){
		T svc = (T) serviceMap.get(name);
		if (svc==null)
			throw new RuntimeException("Can't find service :"+name);
		return svc;
	}

	public static <T> T getService(Class<T> clazz){
		return getService(clazz.getName(),clazz);
	}
	
	/**
	 * @param map
	 */
	public static void init(Map<String, Object> map) {
		serviceMap.putAll(map);
	}
//	/**
//	 * 初始化annotation
//	 */
//	public static void initAnnotation(){
//		PluginEnvirement.getInstance().getAnnotationResolveHelper().resolveAnnotation(serviceMap.values());
//	}
}
