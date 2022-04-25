package net.jplugin.core.kernel.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.RefExtensionMap;
import net.jplugin.core.kernel.kits.RefExtensionPointInferenceKit;

public class AnnoForExtensionMapHandler implements IAnnoForAttrHandler<RefExtensionMap>{

	@Override
	public Class<RefExtensionMap> getAnnoClass() {
		return RefExtensionMap.class;
	}

	@Override
	public Class getAttrClass() {
		return Map.class;
	}

	@Override
	public Object getValue(Object theObject, Class fieldType,Field f,RefExtensionMap anno) {
		String name = anno.pointTo();

		if (StringKit.isNull(name)){
			name = RefExtensionPointInferenceKit.inference(theObject,f, "RefExtension");
		}

		Map objects = PluginEnvirement.getInstance().getExtensionMap(name);
		Map result = new HashMap();
		
		result.putAll(objects);
		return result;
	}

	@Override
	public Object getValue(Object theObject, Class fieldType, RefExtensionMap anno) {
		throw new RuntimeException("can't go here");
	}
}
