package net.jplugin.common.kits;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-22 下午02:09:16
 **/

public class ReflactKit {

	/**
	 * @param extensionObject
	 * @param string
	 * @param propertyList
	 */
	public static Object invoke(Object o, String methodName,
			Object[] args) {
		Method[] methods = o.getClass().getMethods();
		for (Method m:methods){
			if (methodName.equals(m.getName())){
				try {
					return m.invoke(o, args);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new RuntimeException("Can't find the method:"+methodName +" in cls "+o.getClass().getName());
	}
	
	/**
	 * @param type
	 * @param string
	 * @param objects
	 * @return
	 */
	public static Object invoke(Class type,String methodname,Class[] argtypes,
			Object[] args) {
		return invoke(type,null,methodname,argtypes,args);
	}
	
	/**
	 * @param type
	 * @param string
	 * @param objects
	 * @return
	 */
	public static Object invoke(Class type,Object obj, String methodname,Class[] argtypes,
			Object[] args) {
		Method method;
		try {
			method = type.getMethod(methodname, argtypes);
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * @param clazz
	 * @param extensionClass
	 * @return
	 */
	public static boolean isTypeOf(Class child, Class parent) {
//		if (child.equals(parent) )
//			return true;
		return parent.isAssignableFrom(child);
//		Class[] clzs = child.getClasses();
//		for (Class c:clzs){
//			if (c == parent){
//				return true;
//			}
//		}
//		return false;
	}

	
	public static Set<String> getProperties(Class<?> c) {
		return getPropertiesAndType(c).keySet();
	}

	public static Map<String, Class<?>> getDeclaredPropertiesAndType(Class<?> c) {
		return getPropertiesAndType(c.getDeclaredMethods());
	}
	public static Map<String, Class<?>> getPropertiesAndType(Class<?> c) {
		return getPropertiesAndType(c.getMethods());
	}
	/**
	 * @param string
	 * @return
	 */
	private static Map<String, Class<?>> getPropertiesAndType(Method[] marr) {
		Method[] methods = marr;
		Map<String,Class<?>> ret = new HashMap<String, Class<?>>();
		for (Method m:methods){
			String methodName = m.getName();
			if (methodName.length()<=3) continue;
			
			int modf = m.getModifiers();
			
			if ((modf & Modifier.PUBLIC)==0)
				continue;
			
			if ((modf & Modifier.STATIC)!=0)
				continue;
			
			//Object类的方法不考虑
			if (m.getDeclaringClass()==Object.class){
				continue;
			}
			
			if (methodName.startsWith("get")){
				String property = methodName.substring(3);
				property = property.substring(0,1).toLowerCase() + property.substring(1);
				ret.put(property,m.getReturnType());
			}
		}
		return ret;
	}

	/**
	 * @param c
	 * @return
	 */
	public static String getShortName(Class c) {
		String nm = c.getName();
		int pos = nm.lastIndexOf('.');
		if (pos<0) {
			//无包名
			return nm;
		}
		return nm.substring(pos+1);
	}

	/**
	 * @param class1 
	 * @param operation
	 * @return
	 */
	public static Method[] getMethods(Class<? extends Object> clazz, String operation) {
		ArrayList<Method> ret = new ArrayList<Method>(1);
		Method[] methods = clazz.getMethods();
		for (Method m:methods){
			if (m.getName().equals(operation)){
				ret.add(m);
			}
		}
		Method[] arr = new Method[ret.size()]; 
		return ret.toArray(arr);
	}

	/**
	 * @param o
	 * @param key
	 * @param val
	 */
	public static void setProperty(Object o, String key, Object val) {
		String method="set"+key.substring(0,1).toUpperCase()+key.substring(1);
		invoke(o, method, new Object[]{val});
	}

	public static Method findSingeMethodExactly(Class c, String methodName) {
		Method ret = null;
		for (Method m:c.getMethods()){
			if (m.getName().equals(methodName)){
				if (ret == null){
					ret = m;
				}else{
					throw new RuntimeException("find a second method for name:"+methodName +" in "+c.getName());
				}
			}
		}
		return ret;
	}

	/**
	 * @param attr
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public static Method getGetterMethod(Class c,String attr) throws SecurityException, NoSuchMethodException {
		String pName = "get"+Character.toUpperCase(attr.charAt(0)) + attr.substring(1);
		return c.getMethod(pName);
	}



}
