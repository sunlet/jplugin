package net.jplugin.core.kernel.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.management.RuntimeErrorException;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.kernel.api.Extension.Property;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-14 下午12:08:27
 **/

public class PropertyUtil {

	/**
	 * @param o
	 * @param p
	 */
	public static void setProperties(Object o, List<Property> plist) {
		for (Property p:plist){
			ReflactKit.setPropertyFromString(o,p.getKey(),p.getValue());
		}
	}

//	/**
//	 * @param o
//	 * @param key
//	 * @param value
//	 */
//	private static void setProperty(Object o, String key, String value) {
//		Map<String, Class<?>> meta = ReflactKit.getPropertiesAndType(o.getClass());
//		Class type = meta.get(key);
//		if (type==null){
//			throw new RuntimeException("Can't find property :"+key +" in "+o.getClass().getName());
//		}
//		Object val = changeTypeFromString(type,value);
//		ReflactKit.setProperty(o,key,val);
//	}
//
//	/**
//	 * @param type
//	 * @param value
//	 * @return
//	 */
//	private static Object changeTypeFromString(Class type, String value) {
//		if (type == String.class) return value;
//		if (type == int.class || type == Integer.class) return Integer.parseInt(value);
//		if (type == long.class || type == Long.class) return Long.parseLong(value);
//		if (type == double.class || type == float.class || type == Float.class) return Float.parseFloat(value);
//		if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
//		if (type == Character.class || type==char.class) {
//			if (value.length()!=1)throw new RuntimeException("error char value:"+value);
//			return value.charAt(0);
//		}
//		if (type == Class.class){
//			try {
//				return Class.forName(value);
//			} catch (ClassNotFoundException e) {
//				throw new RuntimeException(e);
//			}
//		}
//		if (type.isEnum()){
//			return ReflactKit.invoke(type, "valueOf", new Class[]{String.class},new Object[]{value});
//		}
//		throw new RuntimeException("Primate type not support:"+type.getName());
//	}

}
