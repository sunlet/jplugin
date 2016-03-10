package net.jplugin.core.ctx.api;

import java.lang.reflect.Method;

import net.jplugin.core.ctx.impl.DefaultRuleInvocationHandler;

public class RuleProxyHelper {
	/**
	 * 如果有Rule标记，则执行拦截器，否则直接调用
	 * @param obj
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	public static Object invokeWithRule(Object obj, Method method, Object[] args) throws Throwable{
		Rule rule = method.getAnnotation(Rule.class);
		if (rule==null){
			return method.invoke(obj, args);
		}else{
			return new DefaultRuleInvocationHandler().invoke(null, obj, method, args, rule);
		}
	}
}
