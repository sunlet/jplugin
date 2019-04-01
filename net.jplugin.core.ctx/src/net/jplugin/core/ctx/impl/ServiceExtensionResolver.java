package net.jplugin.core.ctx.impl;

import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.service.api.RefService;
import net.jplugin.core.service.impl.IServiceExtensionResolver;

public class ServiceExtensionResolver implements IServiceExtensionResolver {

	@Override
	public Object resolve(Object theObject, Class fieldType, RefService anno) {
		//查找RuleService
		if (anno.name().equals(""))
			return RuleServiceFactory.getRuleService(fieldType);
		else
			return RuleServiceFactory.getRuleService(anno.name());
	}

}
