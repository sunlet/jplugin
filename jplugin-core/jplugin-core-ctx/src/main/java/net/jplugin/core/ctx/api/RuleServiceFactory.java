package net.jplugin.core.ctx.api;

import java.util.Hashtable;
import java.util.Map;

import net.jplugin.core.ctx.Plugin;
import net.jplugin.core.ctx.impl.proxy.RuleServiceProxyFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 上午09:07:30
 **/

public class RuleServiceFactory {

	private static Hashtable<String, Object> svcMap=new Hashtable<String, Object>();
	public static <T> T getRuleService(Class<T> clz){
		if (PluginEnvirement.INSTANCE.getStateLevel()>=PluginEnvirement.STAT_LEVEL_MADESVC)
			return (T) svcMap.get(clz.getName());
		else
			return (T)RuleServiceProxyFactory.getRuleService(clz.getName(), clz);
	}

	public static Object getRuleService(String svcname){
		return  svcMap.get(svcname);
	}

	public static <T> T getRuleService(String svcname,Class<T> clz){
		if (PluginEnvirement.INSTANCE.getStateLevel()>=PluginEnvirement.STAT_LEVEL_MADESVC)
			return (T) svcMap.get(svcname);
		else
			return (T)RuleServiceProxyFactory.getRuleService(svcname, clz);
	}

	/**
	 */
	public void init() {
		Map<String, Object> objMap = PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_RULE_SERVICE);
		svcMap.putAll(objMap);

		//添加到RuleServiceFactory
		ServiceFactory.initExtensions(PluginEnvirement.getInstance().getExtensionList(Plugin.EP_RULE_SERVICE));

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
