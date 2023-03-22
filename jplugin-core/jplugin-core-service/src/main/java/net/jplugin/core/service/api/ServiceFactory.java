package net.jplugin.core.service.api;

import java.util.*;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午09:55:34
 **/

public class ServiceFactory {
//	static Hashtable<String, Object> serviceMap = new Hashtable<String, Object>();
	
	public static <T> T getService(String name,Class<T> clazz){
//		T svc = (T) serviceMap.get(name);
		T o = (T) _GlobalServiceHolder.get(name);

		if (o==null){
			throw new RuntimeException("Service not found for name:"+name);
		}else{
			if (o.getClass().isArray()){
				StringBuffer sb = new StringBuffer("found multiple service for ["+name+"] :");
				Object[] arr = (Object[]) o;
				for (int i=0;i<arr.length;i++){
					sb.append("\n\t\t").append("[").append(i+1).append("]:");
					sb.append( ((Tuple2<Object,Class>)arr[i]).second.getName()).append(",");
				}
				throw new RuntimeException(sb.toString());
			}else{
				return (T)((Tuple2)o).first;
			}
		}
	}

	public static <T> T getService(Class<T> clazz){
		return getService(clazz.getName(),clazz);
	}


	/**
	 */
	public static void initExtensions(List<Extension> list) {
		for (Extension e:list){
			_addMapping(e.getName(), e.getObject(), e.getFactory().getImplClass());
		}
	}

	public static void _addMapping(String name,Object o,Class objectType){
		if (PluginEnvirement.INSTANCE.getStateLevel()>PluginEnvirement.STAT_LEVEL_MAKINGSVC){
			throw new RuntimeException("can't call");
		}
		_GlobalServiceHolder.addMapping(name, Tuple2.with(o,objectType));
	}


//	/**
//	 * 初始化annotation
//	 */
//	public static void initAnnotation(){
//		PluginEnvirement.getInstance().getAnnotationResolveHelper().resolveAnnotation(serviceMap.values());
//	}
	public static class _GlobalServiceHolder{
		public static Map<String,Object> map = new HashMap<>();

		public static Object get(String name){
			return map.get(name);
		}


		/**
		 * <PRE>
		 * 如何查找一个RefService（属性类型为A)的对象的方法：
		 *     如果A为类，则根据名字查找对应的Service。
		 *     如果A为接口，则查找实现这个接口的类（只查找直接实现的），如果多个实现该接口的Service，则报错。
		 * </PRE>
		 *
		 * <PRE>
		 * 内部实现方法：
		 * 		如果传入了name，则这个object只有一个名字（name）。
		 * 		如果没有传入name，则这个对象有很多个名字:
		 * 			对象对应的类名字
		 * 			对象直接实现的接口类名字（java开头的接口名字除外）
		 *
		 * 		查找Ref的时候，如果通过类型对应触达class的名字找到的是一个数组，则报告冲突。如果不是数组，则返回。
		 * </PRE>
		 * @param name
		 * @param svcInfo
		 */
		public static void addMapping(String name,Tuple2<Object,Class> svcInfo){
			if (svcInfo.second.isArray())
				throw new RuntimeException("Array service is not supported. "+svcInfo.second.getName());
			if (svcInfo.first==null) throw new RuntimeException("null object.");

			if (StringKit.isNotNull(name)){
				add(name,svcInfo);
			}else{
				List<String> names = getNames(svcInfo.second);
				for (String n:names){
					add(n,svcInfo);
				}
			}
		}

		private static List<String> getNames(Class<?> clazz) {
				List<String> list = new ArrayList<>(4);
				list.add(clazz.getName());
				Class<?>[] interfaces = clazz.getInterfaces();

				Arrays.stream(interfaces).forEach(c->{
					if (!c.getName().startsWith("java.")){
						list.add(c.getName());
					}
				});
				return list;
		}

		private static void add(String name,Tuple2<Object,Class> objInfo){
			Object value = map.get(name);
			if (value!=null){
				// 这个名字已经有了
				if (value.getClass().isArray()){
					//追加一个数组项
					map.put(name, expandArray((Object[]) value,objInfo));
				}else{
					//变成数组
					map.put(name,new Object[]{value,objInfo});
				}
			}else{
				map.put(name,objInfo);
			}
		}

		private static Object[] expandArray(Object[] arr, Object obj) {
			//复制旧数组
			Object[] newarr = new Object[arr.length+1];
			for (int i=0;i<arr.length;i++){
				newarr[i] = arr[i];
			}

			//追加新value
			newarr[arr.length] = obj;
			return newarr;
		}



}
}
