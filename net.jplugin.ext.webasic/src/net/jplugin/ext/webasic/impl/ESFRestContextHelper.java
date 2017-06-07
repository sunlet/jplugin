package net.jplugin.ext.webasic.impl;

import net.jplugin.core.kernel.api.ctx.RequesterInfo.Content;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;

public class ESFRestContextHelper {

	public static void fillContentForRestful(CallParam cp, ESFRestContext ctx) {
		fillContent(cp);
		
		//from ctx
		RequesterInfo info = ThreadLocalContextManager.getRequestInfo();
		//ipaddress
		info.setCallerIpAddress(ctx.getCallerIpAddress());
		//reqeusturl
		info.setRequestUrl(ctx.getRequestUrl());
		//cookies
		info.getCookies()._setFromMap(ctx.getCookieMap());
		//headers
		info.getHeaders()._setFromMap(ctx.getHeaderMap());
	}

	private static void fillContent(CallParam cp) {
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
