package net.jplugin.core.config.impl;

import net.jplugin.common.kits.ObjectKit;
import net.jplugin.common.kits.PritiveKits;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.config.api.RefConfig;
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
	public Object getValue(Object theObject, Class fieldType, RefConfig anno) {
		String val = ConfigFactory.getStringConfig(anno.path(),anno.defaultValue());
		return PritiveKits.getTransformer(fieldType).fromString(fieldType,val);
	}


}
