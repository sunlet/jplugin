package net.jplugin.core.ctx.impl.proxy;

import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.service.api.ServiceFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;

public class RuleServiceProxyFactory {
	//这里并发不高，只在初始化使用，用Hashtable
	//发生重新创建Proxy对象，也是没有问题的
	static Hashtable<String, Object> map=new Hashtable();
	
	public static Object getRuleService(String name, Class type){
		Object o = map.get(name);

		if (o!=null) 
			return o;
		else{
			o = Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new InvocationHandler4RuleService(name));
			map.put(name, o);
			return o;
		}
	}
	
	static class InvocationHandler4RuleService implements InvocationHandler{
		private String name;

		InvocationHandler4RuleService(String nm){
			this.name = nm;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object real;
			real = RuleServiceFactory.getRuleService(name);

			if (PluginEnvirement.INSTANCE.getStateLevel()<PluginEnvirement.STAT_LEVEL_MADESVC)
				throw new RuntimeException("can't invoke before rule service is made!" +name);

			if (real==null) {
				throw new RuntimeException("can't find service for name:"+name);
			}else {
				return method.invoke(real, args);
			}
		}
	}
}
