package net.jplugin.core.ctx.api;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import net.jplugin.core.ctx.Plugin;
import net.jplugin.core.ctx.impl.DefaultRuleInvocationHandler;
import net.jplugin.core.ctx.impl.RuleInterceptor;
import net.jplugin.core.kernel.api.ExtensionObjects;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 上午09:07:30
 **/

public class RuleServiceFactory {

	private static Hashtable<String, Object> svcMap=new Hashtable<String, Object>();
	public static <T> T getRuleService(Class<T> clz){
		return (T) svcMap.get(clz.getName());
	}

	public static Object getRuleService(String svcname){
		return  svcMap.get(svcname);
	}

	public static <T> T getRuleService(String svcname,Class<T> clz){
		return (T) svcMap.get(svcname);
	}

	/**
	 */
	public void init() {
		Map<String, Object> objMap = PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_RULE_SERVICE);
		svcMap.putAll(objMap);
	}

	/**
	 * @param blName
	 * @return 
	 */
	public static Class<?> getRuleInterface(String blName) {
		try {
			return Class.forName(blName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("the name must be the interface",e);
		}
	}
}
