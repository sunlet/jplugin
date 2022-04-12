package net.jplugin.core.kernel.impl;

import java.lang.reflect.Field;

import net.jplugin.core.kernel.api.Beans;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.kernel.api.RefBean;

public class AnnoForBeanHandler implements IAnnoForAttrHandler<RefBean> {
	@Override
	public Class<RefBean> getAnnoClass() {
		return RefBean.class;
	}

	@Override
	public Class getAttrClass() {
		return Object.class;
	}

	@Override
	public Object getValue(Object theObject, Class fieldType, RefBean anno) {
		String name = anno.id();
		Object obj = Beans.get(name);
		if (fieldType.isAssignableFrom(obj.getClass())) {
			return obj;
		}else {
			throw new RuntimeException("FieldType is :"+fieldType.getName()+", bug bean type is :"+obj.getClass().getName());
		}
	}
}
