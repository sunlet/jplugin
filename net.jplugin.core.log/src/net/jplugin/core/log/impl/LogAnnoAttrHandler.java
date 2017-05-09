package net.jplugin.core.log.impl;

import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;

public class LogAnnoAttrHandler implements IAnnoForAttrHandler<RefLogger> {

	public Class<RefLogger> getAnnoClass() {
		return RefLogger.class;
	}

	public Class getAttrClass() {
		return Logger.class;
	}

	public Object getValue(Object theObject,Class fieldType, RefLogger anno) {
		if ("".equals(anno.value())){
			return LogFactory.getLogger(theObject.getClass());
		}else{
			return LogFactory.getLogger(anno.value());
		}
	}

}
