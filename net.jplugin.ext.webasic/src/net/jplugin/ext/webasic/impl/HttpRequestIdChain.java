package net.jplugin.ext.webasic.impl;

import java.io.IOException;

import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.common.kits.http.filter.HttpClientFilterChain;
import net.jplugin.common.kits.http.filter.HttpFilterContext;
import net.jplugin.common.kits.http.filter.IHttpClientFilter;

public class HttpRequestIdChain implements IHttpClientFilter {

	//TODO:这个方法还没有实现。。。。。。
	public String filter(HttpClientFilterChain fc, HttpFilterContext ctx) throws IOException, HttpStatusException {
		return fc.next(ctx);
	}

}
