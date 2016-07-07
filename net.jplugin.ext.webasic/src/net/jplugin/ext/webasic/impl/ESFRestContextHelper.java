package net.jplugin.ext.webasic.impl;

import net.jplugin.core.kernel.api.ctx.RequesterInfo.Content;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;

public class ESFRestContextHelper {

	public static void fillContentForRestful(CallParam cp) {
		Content content = ThreadLocalContextManager.getRequestInfo().getContent();
		if (cp.getCallType()==CallParam.CALLTYPE_JSON){
			content.setContentType("application/json");
			content.setJsonContent(cp.getParamMap().get(CallParam.JSON_KEY));
		}else{
			content.setContentType("");// now as this
			content.setParamContent(cp.getParamMap());
		}
	}
}
