package net.jplugin.core.kernel.api;

import java.lang.reflect.Field;

public interface IAnnoForAttrHandler <T> {
	/**
	 * 标记该Handler对应的AnnoClass
	 * @return
	 */
	public Class<T> getAnnoClass();
	
	/**
	 * 用来做校验，对应的annotation对应这些属性类型
	 * @return
	 */
	public Class getAttrClass();

	/**
	 * 获取值
	 * @param fieldType Attr的实际类型
	 * @param anno  anno对象
	 * @return 
	 */
	public Object  getValue(Object theObject,Class fieldType,T anno);
	
	/**
	 * 获取值, 4 param
	 * @param fieldType Attr的实际类型
	 * @param anno  anno对象
	 * @return 
	 */
	default Object  getValue(Object theObject,Class fieldType,Field f, T anno) {
		return getValue(theObject,fieldType,anno);
	}
}
