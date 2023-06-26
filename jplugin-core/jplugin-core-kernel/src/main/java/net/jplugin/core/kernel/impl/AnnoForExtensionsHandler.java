package net.jplugin.core.kernel.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.RefExtensions;
import net.jplugin.core.kernel.kits.RefExtensionPointInferenceKit;

public class AnnoForExtensionsHandler implements IAnnoForAttrHandler<RefExtensions>{

	@Override
	public Class<RefExtensions> getAnnoClass() {
		return RefExtensions.class;
	}

	@Override
	public Class getAttrClass() {
		return List.class;
	}

	@Override
	public Object getValue(Object theObject, Class fieldType, RefExtensions anno) {
		throw new RuntimeException("can't go here");
	}

	@Override
	public Object getValue(Object theObject, Class fieldType, Field f, RefExtensions anno) {
		String name = anno.pointTo();

		if (StringKit.isNull(name)){
			name = RefExtensionPointInferenceKit.inference(theObject,f, "RefExtension");
		}

		Object[] objects = PluginEnvirement.getInstance().getExtensionObjects(name);
		List result = new ArrayList<>(objects.length);
		
		for (int i=0;i<objects.length;i++){
			result.add(objects[i]);
		}
		return result;
	}

}
