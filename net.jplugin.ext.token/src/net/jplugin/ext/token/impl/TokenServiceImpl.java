package net.jplugin.ext.token.impl;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.CalenderKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.core.das.hib.api.IDataService;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.token.api.ITokenService;

public class TokenServiceImpl implements ITokenService {

	@Override
	public String createToken(Map<String, String> tkInfo, String identifier,
			String realm) {
		IDataService das = ServiceFactory.getService(IDataService.class);
		DBToken tk = new DBToken();
		tk.setIdentifier(identifier);
		tk.setRealm(realm);
		tk.setTokenInfo(JsonKit.object2Json(tkInfo));
		tk.setRandom(String.valueOf(genRandom()));
		tk.setCreateTime(CalenderKit.getTimeString(System.currentTimeMillis()));
		das.insert(tk);
		
		das.executeDeleteSql("delete from DBTOKEN where identifier=? and realm=? and id<>?",identifier,realm,tk.getId());
		return tk.getId()+"@"+tk.getRandom();
	}
	
	static Random rd = new Random();
	private static long genRandom() {
		long next = rd.nextLong();
		return next;
	}

	@Override
	public void putTokenInfo(String tk, Map<String, String> info) {
		String tkid = getKey(tk);
		IDataService das = ServiceFactory.getService(IDataService.class);
		DBToken token = das.findById(DBToken.class, tkid);
		if (token==null) throw new RuntimeException("can't find the token."+tkid);
		String old = token.getTokenInfo();
		Map<String,String> newInfo = JsonKit.json2Map(old);
		newInfo.putAll(info);
		token.setTokenInfo(JsonKit.object2Json(newInfo));
	}

	private String getKey(String tk) {
		int pos = tk.indexOf('@');
		AssertKit.assertTrue(pos > 0 );
		return tk.substring(0, pos);
	}

	@Override
	public void removeTokenInfo(String tk, Set<String> keys) {
		String tkid = getKey(tk);
		IDataService das = ServiceFactory.getService(IDataService.class);
		DBToken token = das.findById(DBToken.class, tkid);
		if (token==null) throw new RuntimeException("can't find the token."+tkid);
		String old = token.getTokenInfo();
		Map<String,String> newInfo = JsonKit.json2Map(old);
		for (String key:keys){
			newInfo.remove(key);
		}
		token.setTokenInfo(JsonKit.object2Json(newInfo));
	}

	@Override
	public Map<String, String> validAndGetTokenInfo(String tk) {
		String tkid = getKey(tk);
		IDataService das = ServiceFactory.getService(IDataService.class);
		DBToken token = das.findById(DBToken.class, tkid);
		if (token==null) return null;
		
		String random = getRandom(tk);
		if (random==null) return null;
		if (!random.equals(token.getRandom())) return null;
		
		return JsonKit.json2Map(token.getTokenInfo());
	}

	private String getRandom(String tk) {
		int pos = tk.indexOf('@');
		AssertKit.assertTrue(pos > 0 );
		return tk.substring(pos+1);
	}

	@Override
	public void removeToken(String tk) {
		String tkid = getKey(tk);
		IDataService das = ServiceFactory.getService(IDataService.class);
		das.delete(DBToken.class,tkid);
	}

	@Override
	public Map<String, String> getTokenInfo(String tk) {
		return validAndGetTokenInfo(tk);
	}
}
