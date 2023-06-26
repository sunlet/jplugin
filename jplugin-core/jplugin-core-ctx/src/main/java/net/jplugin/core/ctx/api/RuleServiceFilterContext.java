package net.jplugin.core.ctx.api;

import java.lang.reflect.Method;

public class RuleServiceFilterContext {
	Object proxyObject;
	Object object;
	Method method;
	Method proceedMethod;
	Object[] args;
	Rule annotation;
	public static RuleServiceFilterContext create(Object p,Object o,Method m,Method proceed,Object[] arg,Rule anno){
		RuleServiceFilterContext ret = new RuleServiceFilterContext();
		ret.proxyObject = p;
		ret.object = o;
		ret.method = m;
		ret.args = arg;
		ret.annotation = anno;
		ret.proceedMethod = proceed;
		return ret;
	}
	public Object getProxyObject() {
		return proxyObject;
	}
	public Object getObject() {
		return object;
	}
	public Method getMethod() {
		return method;
	}
	public Object[] getArgs() {
		return args;
	}
	public Rule getAnnotation() {
		return annotation;
	}

	public Method getProceedMethod(){
		return proceedMethod;
	}
	

}
