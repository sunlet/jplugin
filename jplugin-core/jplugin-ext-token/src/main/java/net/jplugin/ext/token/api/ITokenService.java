package net.jplugin.ext.token.api;

import java.util.Map;
import java.util.Set;

import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.api.Rule.TxType;


public interface ITokenService {

	/**
	 * 一个identifier在一个realm中只能有一个token存在，后面创建的token将会自动挤出以前创建的。
	 * @param tkInfo
	 * @param identifier
	 * @param realm
	 * @return
	 */
	@Rule(methodType=TxType.REQUIRED)
	public String createToken(Map<String,String> tkInfo,String identifier,String realm);
	/**
	 * 如果key不存在，抛出异常
	 * @param tk
	 * @param info
	 */
	@Rule(methodType=TxType.REQUIRED)
	public void putTokenInfo(String tk,Map<String,String> info); 
	/**
	 * 如果key不存在，抛出异常
	 * @param tk
	 * @param keys
	 */
	@Rule(methodType=TxType.REQUIRED)
	public void removeTokenInfo(String tk,Set<String> keys);
	
	/**
	 * 如果key不存在，返回null；如果key存在但是无信息，返回空map
	 * @param tk
	 * @return
	 */
	@Rule
	public Map<String,String> validAndGetTokenInfo(String tk);
	
	@Rule
	public Map<String,String> getTokenInfo(String tk);
	
	@Rule(methodType=TxType.REQUIRED)
	public void removeToken(String tk);
	
}
