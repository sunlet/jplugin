package net.jplugin.core.ctx.api;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import net.jplugin.core.ctx.impl.DefaultRuleInvocationHandler;
import net.jplugin.core.ctx.impl.RuleInterceptor;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 上午09:07:30
 **/

public class RuleServiceFactory {

	private static Hashtable<String, Object> svcMap=new Hashtable<String, Object>();
	private static Map<String,RuleServiceDefinition> serviceDefine=null;
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
	 * @param defs
	 */
	public void init(Map<String, RuleServiceDefinition> defs) {
		this.serviceDefine = defs;
		
		for (Entry<String, RuleServiceDefinition> en:defs.entrySet()){
			RuleServiceDefinition def = (RuleServiceDefinition) en.getValue();
			Object realImpl;
			try {
				def.valid();
				realImpl = def.getImpl().newInstance();
				PluginEnvirement.INSTANCE.resolveRefAnnotation(realImpl);
			} catch (Exception e){
				throw new CtxRuntimeException("Create proxy failed",e);
			}
			Object proxy = RuleInterceptor.getProxyInstance(def.getInterf(),realImpl,new DefaultRuleInvocationHandler());
			svcMap.put(en.getKey(),proxy);
		}
//		for (int i=0;i<defs.length;i++){
//			RuleServiceDefinition def = defs[i];
//			Object realImpl;
//			try {
//				realImpl = def.getRuleImplementation().newInstance();
//			} catch (Exception e){
//				throw new CtxRuntimeException("Create proxy failed",e);
//			}
//			Object proxy = RuleInterceptor.getProxyInstance(def.getRuleInterface(),realImpl,new DefaultRuleInvocationHandler());
//			svcMap.put(def.getRuleInterface(),proxy);
//		}
	}

	/**
	 * @param blName
	 * @return 
	 */
	public static Class<?> getRuleInterface(String blName) {
		return serviceDefine.get(blName).getInterf();
	}
}
