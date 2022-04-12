package net.jplugin.core.rclient.proxyfac;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.rclient.Plugin;
import net.jplugin.core.rclient.api.ITokenFetcher;

public class TokenFactory {
	static ITokenFetcher tokenFetcher;
	public static void init(){
		ITokenFetcher[] arr = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_TOKEN_FETCHER, ITokenFetcher.class);
		if (arr.length>1){
			throw new RuntimeException("Too many token fetcher founded!");
		}
		if (arr.length==1)
			tokenFetcher = arr[0];
	}
	
	public static String getAppToken(){
		if (tokenFetcher==null) return null;
		else return tokenFetcher.fetchToken();
	}
}
