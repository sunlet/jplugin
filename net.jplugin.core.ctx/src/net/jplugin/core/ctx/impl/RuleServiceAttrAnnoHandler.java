package net.jplugin.core.ctx.impl;

import net.jplugin.core.ctx.api.RefRuleService;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;

public class RuleServiceAttrAnnoHandler implements IAnnoForAttrHandler<RefRuleService> {

	public Class<RefRuleService> getAnnoClass() {
		return RefRuleService.class;
	}

	public Class getAttrClass() {
		return Object.class;
	}

	public Object getValue(Object theObject, Class fieldType, RefRuleService anno) {
		if (anno.name().equals(""))
			return RuleServiceFactory.getRuleService(fieldType);
		else
			return RuleServiceFactory.getRuleService(anno.name());
	}
}
