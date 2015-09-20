package net.jplugin.ext.token.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class TokenMockSession{

	public static Map<String, String> getAllAttributes(){
		String token=ThreadLocalContextManager.getRequestInfo().getToken();
		if (token==null){
			throw new RuntimeException("token is null");
		}
		ITokenService tksvc = RuleServiceFactory.getRuleService(ITokenService.class);
		Map<String, String> tkinfo = tksvc.getTokenInfo(token);
		return tkinfo;
	}
	
	public static String getAttribute(String key){
		String token=ThreadLocalContextManager.getRequestInfo().getToken();
		if (token==null){
			throw new RuntimeException("token is null");
		}
		ITokenService tksvc = RuleServiceFactory.getRuleService(ITokenService.class);
		Map<String, String> tkinfo = tksvc.getTokenInfo(token);
		return tkinfo.get(key);
	}
	public static void setAttribute(String key,String val){
		String token=ThreadLocalContextManager.getRequestInfo().getToken();
		if (token==null){
			throw new RuntimeException("token is null");
		}
		ITokenService tksvc = RuleServiceFactory.getRuleService(ITokenService.class);
		Map<String, String> tkinfo = new HashMap<String, String>();
		tkinfo.put(key, val);
		tksvc.putTokenInfo(token, tkinfo);
	}
	public static void removeAttribute(String string) {
		String token=ThreadLocalContextManager.getRequestInfo().getToken();
		if (token==null){
			throw new RuntimeException("token is null");
		}
		ITokenService tksvc = RuleServiceFactory.getRuleService(ITokenService.class);
		Set<String> set = new HashSet<String>();
		set.add(string);
		tksvc.removeTokenInfo(token, set);
	}
	public static void invalid() {
		return;
	}

	public static void removeAttributes(HashSet toRemove) {
		String token=ThreadLocalContextManager.getRequestInfo().getToken();
		if (token==null){
			throw new RuntimeException("token is null");
		}
		ITokenService tksvc = RuleServiceFactory.getRuleService(ITokenService.class);
		tksvc.removeTokenInfo(token, toRemove);
	}
}
