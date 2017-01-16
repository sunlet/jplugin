package net.jplugin.common.kits.http.filter;

import java.io.IOException;

import net.jplugin.common.kits.http.HttpStatusException;

public interface IHttpClientFilter {
	public String filter(HttpClientFilterChain fc, HttpFilterContext ctx) throws IOException, HttpStatusException;
}
