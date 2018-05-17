package net.jplugin.core.service.impl;

import net.jplugin.core.service.api.RefService;

public interface IServiceExtensionResolver {

	public  Object resolve(Object theObject, Class fieldType, RefService anno);

}
