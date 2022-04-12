package net.jplugin.core.kernel.api;

import java.util.Map;

/**
 * 本类帮助获取一个扩展点下的扩展形成的数组或者Map
 * <li>getNamedExtensions: 获取NAMED类型的扩展结果
 * <li>getListExtensions:  获取LIST类型的扩展结果
 * <li>getUniqueExtension:  扩展UNIQUE类型的扩展结果
 * 
 * @author LiuHang
 */

public class ExtensionPointHelper {

	/**
	 * 获取一个扩展点pointName下的所有扩展形成的Map, key为扩展点名字,value为扩展的对象. 以后请调用  getNamedExtensions方法
	 * @param pointName
	 * @return
	 */
	@Deprecated
	public static Map<String, Object> getExtensionMap(String pointName) {
		return getNamedExtensions(pointName);
	}
	/**
	 * 获取一个扩展点pointName下的所有扩展形成的Map, key为扩展点名字,value为扩展的对象.
	 * @param pointName
	 * @return
	 */
	public static Map<String, Object> getNamedExtensions(String pointName) {
		return PluginEnvirement.getInstance().getExtensionMap(pointName);
	}
	/**
	 * 获取一个扩展点pointName下的所有扩展形成的Map, key为扩展点名字,value为扩展的对象. 并进行泛型类型转化。以后请调用  getNamedExtensions方法
	 * @param <T>
	 * @param pointName
	 * @param t 
	 * @return
	 */
	@Deprecated
	public static <T> Map<String,T> getExtensionMap(String pointName,Class<T> t){
		return getNamedExtensions(pointName, t);
	}
	
	/**
	 * 获取一个扩展点pointName下的所有扩展形成的Map, key为扩展点名字,value为扩展的对象. 并进行泛型类型转化。
	 * @param <T>
	 * @param pointName
	 * @param t 
	 * @return
	 */
	public static <T> Map<String,T> getNamedExtensions(String pointName,Class<T> t){
		return PluginEnvirement.getInstance().getExtensionMap(pointName,t);
	}
	
	/**
	 * 获取一个扩展点pointName下的所有扩展形成的数组. 以后请调用  getListExtensions方法
	 * @param pointName
	 * @return
	 */
	@Deprecated
	public static Object[] getExtensionObjects(String pointName) {
		return getListExtensions(pointName);
	}
	
	/**
	 * 获取一个扩展点pointName下的所有扩展形成的数组.
	 * @param pointName
	 * @return
	 */
	public static Object[] getListExtensions(String pointName) {
		return PluginEnvirement.getInstance().getExtensionObjects(pointName);
	}
	
	/**
	 * 获取一个扩展点pointName下的所有扩展形成的数组.并进行泛型类型转化。以后请调用  getListExtensions方法
	 * @param <T>
	 * @param pointName
	 * @param t
	 * @return
	 */
	@Deprecated 
	public static <T> T[] getExtensionObjects(String pointName, Class<T> t) {
		return getListExtensions(pointName, t);
	}
	
	/**
	 * 获取一个扩展点pointName下的所有扩展形成的数组.并进行泛型类型转化。
	 * @param <T>
	 * @param pointName
	 * @param t
	 * @return
	 */
	public static <T> T[] getListExtensions(String pointName, Class<T> t) {
		return PluginEnvirement.getInstance().getExtensionObjects(pointName,t);
	}
	
	/**
	 * 获取单个Extension,扩展点的类型需要是Singleton类型. 以后请调用  getUniqueExtension方法 
	 * @param <T>
	 * @param pointName
	 * @param t
	 * @return
	 */
	@Deprecated
	public static <T> T getExtension(String pointName,Class<T> t) {
		return getUniqueExtension(pointName, t);
	}
	/**
	 * 获取单个Extension,扩展点的类型需要是UNIQUE类型
	 * @param <T>
	 * @param pointName
	 * @param t
	 * @return
	 */
	public static <T> T getUniqueExtension(String pointName,Class<T> t) {
		return PluginEnvirement.getInstance().getExtension(pointName,t);
	}
}
