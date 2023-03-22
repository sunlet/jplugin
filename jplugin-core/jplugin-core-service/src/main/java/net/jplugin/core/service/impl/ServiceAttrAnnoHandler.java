package net.jplugin.core.service.impl;

import java.lang.reflect.Field;

import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.service.api.RefService;
import net.jplugin.core.service.api.ServiceFactory;

public class ServiceAttrAnnoHandler implements IAnnoForAttrHandler<RefService> {

//	public static IServiceExtensionResolver serviceExtensionResolver;

	public Class<RefService> getAnnoClass() {
		return RefService.class;
	}

	public Class getAttrClass() {
		return Object.class;
	}

	/**
	 * 先查找RuleService，再查找Service
	 */
	public Object getValue(Object theObject, Class fieldType, RefService anno) {
		Object o=null;
//		try {
//			// 发生查找错误，不要抛出异常，后续查找
//			if (serviceExtensionResolver != null) {
//				o = serviceExtensionResolver.resolve(theObject, fieldType, anno);
//			}
//		} catch (RuntimeException e) {
//			o = null;
//		}
		
		if (o == null){
			o = get(fieldType,anno);
		}
		return o;
	}

	private Object get(Class fieldType, RefService anno) {

		if (anno.name().equals(""))
			return ServiceFactory.getService(fieldType);
		else
			return ServiceFactory.getService(anno.name(), fieldType);

	}
}
