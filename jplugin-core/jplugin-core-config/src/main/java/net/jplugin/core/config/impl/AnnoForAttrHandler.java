package net.jplugin.core.config.impl;

import java.lang.reflect.Field;

import net.jplugin.common.kits.ObjectKit;
import net.jplugin.common.kits.PritiveKits;
import net.jplugin.core.config.api.CompConfigFactory;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.config.api.RefConfig;
import net.jplugin.core.config.impl.autofresh.RefConfigAutoRefresher;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;

public class AnnoForAttrHandler implements IAnnoForAttrHandler<RefConfig> {

	@Override
	public Class<RefConfig> getAnnoClass() {
		return RefConfig.class;
	}

	@Override
	public Class getAttrClass() {
		return Object.class;
	}

	@Override
	public Object getValue(Object theObject, Class fieldType, Field f,RefConfig anno) {
		
		RefConfigAutoRefresher.instance.handleAutoFresh(theObject,f,anno);
		
		String val = getStringConfigValue(theObject,anno.path(),anno.defaultValue());
		return PritiveKits.getTransformer(fieldType).fromString(fieldType,val);
	}

	private String getStringConfigValue(Object theObject, String path, String defaultValue) {
//		if (theObject.getClass().getName().startsWith("net.jplugin"))
//			return ConfigFactory.getStringConfig(path,defaultValue);
//		else
			return CompConfigFactory.getStringConfig(path,defaultValue);
	}

	@Override
	public Object getValue(Object theObject, Class fieldType, RefConfig anno) {
		throw new RuntimeException("shoudln't called");
	}

}
