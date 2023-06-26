package net.jplugin.core.kernel.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.jplugin.common.kits.Comparor;
import net.jplugin.common.kits.SortUtil;
import net.jplugin.core.kernel.api.PluginAnnotation;

public class PluginPrepareHelper {
	/**
	 * 目前调用prepare是无序的
	 */
//	private static Class needPrepare(String obj) {
//		try {
//			Class clazz = Class.forName(obj);
//			Method method = clazz.getMethod("prepare", new Class[] {});
//			if ((method.getModifiers() & Modifier.STATIC) > 0) {
//				return clazz;
//			} else {
//				System.out.println("Important Warnning:plugin " + obj + " not prepared,method not static");
//				return null;
//			}
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Plugin class not found:" + obj);
//		} catch (NoSuchMethodException e) {
//			return null;
//			// its normal
//		} catch (SecurityException e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}

	private static Class needPrepare(Class c) {
		try {
			Class clazz = c;
			Method method = clazz.getMethod("prepare", new Class[] {});
			if ((method.getModifiers() & Modifier.STATIC) > 0) {
				return clazz;
			} else {
				System.out.println("Important Warnning:plugin " + c.getName() + " not prepared,method not static");
				return null;
			}
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Plugin class not found:" + c.getName());
		} catch (NoSuchMethodException e) {
			return null;
			// its normal
		} catch (SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 目前调用prepare是无序的
	 */
	private static void classPrepare(int index, Class clazz) {
		try {
			System.out.println("PreparePlugin : [" + index + "] " + clazz.getName());
			Method method = clazz.getMethod("prepare", new Class[] {});
			method.invoke(null, new Object[] {});
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e.getTargetException());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void preparePlugins(List<Class> pluginToLoad) {
		List<Class> classNeedPrepare = new ArrayList();
		for (Class obj : pluginToLoad) {
			Class c = needPrepare(obj);
			if (c != null)
				classNeedPrepare.add(c);
		}
		SortUtil.sort(classNeedPrepare, new Comparor() {
			public boolean isGreaterThen(Object o1, Object o2) {
				Class c1 = (Class) o1;
				Class c2 = (Class) o2;
				PluginAnnotation a1 = (PluginAnnotation) c1.getAnnotation(PluginAnnotation.class);
				PluginAnnotation a2 = (PluginAnnotation) c2.getAnnotation(PluginAnnotation.class);
				int s1 = a1 == null ? 0 : a1.prepareSeq();
				int s2 = a2 == null ? 0 : a2.prepareSeq();
				return s1 > s2;
			}
		});
		
		for (int i=0;i<classNeedPrepare.size();i++){
			classPrepare(i,classNeedPrepare.get(i));
		}
		
	}

//	public static void preparePlugins(Set<Object> pluginToLoad) {
//		List<Class> classNeedPrepare = new ArrayList();
//		for (Object obj : pluginToLoad) {
//			Class c = needPrepare((String) obj);
//			if (c != null)
//				classNeedPrepare.add(c);
//		}
//		SortUtil.sort(classNeedPrepare, new Comparor() {
//			public boolean isGreaterThen(Object o1, Object o2) {
//				Class c1 = (Class) o1;
//				Class c2 = (Class) o2;
//				PluginAnnotation a1 = (PluginAnnotation) c1.getAnnotation(PluginAnnotation.class);
//				PluginAnnotation a2 = (PluginAnnotation) c2.getAnnotation(PluginAnnotation.class);
//				int s1 = a1 == null ? 0 : a1.prepareSeq();
//				int s2 = a2 == null ? 0 : a2.prepareSeq();
//				return s1 > s2;
//			}
//		});
//
//		for (int i=0;i<classNeedPrepare.size();i++){
//			classPrepare(i,classNeedPrepare.get(i));
//		}
//
//	}
}
