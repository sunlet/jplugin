package net.luis.main;

import net.jplugin.core.rclient.api.IServiceUrlResolver;

public class TestServciceUrlResolver implements IServiceUrlResolver {
	public String resolve(String protocol, String baseUrl) {
		return baseUrl;
	}
}
