package net.jplugin.ext.webasic.impl;

import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class ESFRestContext {
	String callerIpAddress;
	String requestUrl;

	public String getCallerIpAddress() {
		return callerIpAddress;
	}

	public void setCallerIpAddress(String callerIpAddress) {
		this.callerIpAddress = callerIpAddress;
	}

	public static void fill(ESFRestContext ctx) {
		//from ctx
		RequesterInfo info = ThreadLocalContextManager.getRequestInfo();
		info.setCallerIpAddress(ctx.getCallerIpAddress());
		info.setRequestUrl(ctx.getRequestUrl());
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

}
