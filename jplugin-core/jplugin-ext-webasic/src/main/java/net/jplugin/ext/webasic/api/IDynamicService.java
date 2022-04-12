package net.jplugin.ext.webasic.api;

import net.jplugin.core.kernel.api.ctx.RequesterInfo;

public interface IDynamicService {
	public Object execute(RequesterInfo requestInfo, String dynamicPath);
}

