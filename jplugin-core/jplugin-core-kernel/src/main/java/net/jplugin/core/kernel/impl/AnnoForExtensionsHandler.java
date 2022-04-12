package net.jplugin.core.kernel.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.RefExtensions;

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
	public Object getValue(Object theObject, Class fieldType,RefExtensions anno) {
		String name = anno.pointTo();
		

		Object[] objects = PluginEnvirement.getInstance().getExtensionObjects(name);
		List result = new ArrayList<>(objects.length);
		
		for (int i=0;i<objects.length;i++){
			result.add(objects[i]);
		}
		return result;
	}

}
