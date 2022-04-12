package net.jplugin.core.das.api.dds;

import java.util.Map;

import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class DDSManager {

	public static DDSManager me = new DDSManager();
	private Map<String, Object> types;
	

	public void init() {
		types = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.core.das.Plugin.EP_DYNAMIC_DATASOURCE_TYPE);
	}
	
	public Class getDataSourceClassByType(String type) {
		ClassDefine ret = (ClassDefine) types.get(type);
		if (ret==null)
			throw new RuntimeException("Not registed route datasource type:"+type);
		return ret.getClazz();
	}

}
