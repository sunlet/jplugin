package net.jplugin.core.kernel.impl;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.RefExtension;
import net.jplugin.core.kernel.kits.RefExtensionPointInferenceKit;

import java.lang.reflect.Field;

public class AnnoForExtensionHandler implements IAnnoForAttrHandler<RefExtension>{

	@Override
	public Class<RefExtension> getAnnoClass() {
		return RefExtension.class;
	}

	@Override
	public Class getAttrClass() {
		return Object.class;
	}

	@Override
	public Object getValue(Object theObject, Class fieldType, Field f, RefExtension anno) {
		String name = anno.pointTo();

		if (StringKit.isNull(name)){
			name = RefExtensionPointInferenceKit.inference(theObject,f,"RefExtension");
		}

		Object object = PluginEnvirement.getInstance().getExtension(name,Object.class);
		return object;
	}

	@Override
	public Object getValue(Object theObject, Class fieldType,RefExtension anno) {
		throw new RuntimeException("can't go here");
	}

}
