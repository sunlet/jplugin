package net.jplugin.ext.webasic.impl;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import net.jplugin.core.ctx.api.RuleProxyHelper;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.api.MethodFilterContext;
import net.jplugin.ext.webasic.impl.WebDriver.ControllerMeta;
import net.jplugin.ext.webasic.impl.filter.IMethodCallback;
import net.jplugin.ext.webasic.impl.filter.service.ServiceFilterManager;
import net.jplugin.ext.webasic.impl.restm.RestMethodControllerSet4Invoker;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;
import net.jplugin.ext.webasic.impl.restm.invoker.IServiceInvoker;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvokerSet;
import net.jplugin.ext.webasic.impl.rmethod.RmethodControllerSet4Invoker;

public class ESFHelper {
	
	/**
	 * RPC调用这个方法
	 * @param obj
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	public static Object invokeWithRule(String servicePath,final Object obj, final Method method, final Object[] args) throws Throwable{
		try{
			ThreadLocalContextManager.instance.createContext();
			MethodFilterContext sfc = new MethodFilterContext(servicePath, obj, method, args);
			
			return ServiceFilterManager.INSTANCE.executeWithFilter(sfc,new IMethodCallback() {
				public Object run() throws Throwable {
					return RuleProxyHelper.invokeWithRule(obj, method, args);
				}
			});
		}finally{
			ThreadLocalContextManager.instance.releaseContext();
		}
	}
	
	/**
	 * Restful调用这个方法
	 * @param cp
	 * @throws Throwable
	 */
	public static void callRestfulService(CallParam cp)  throws Throwable{
		try{
			ThreadLocalContextManager.instance.createContext();
			ServiceInvokerSet.instance.call(cp);
		}finally{
			ThreadLocalContextManager.instance.releaseContext();
		}
	}
	/**
	 * 根据URI获取到对应的JavaBean
	 * @param cm
	 * @param arg
	 * @return
	 */
	public static Object getObject(String uri) {
		ControllerMeta cm = WebDriver.INSTANCE.parseControllerMeta(uri);
		
		IControllerSet cs = cm.getControllerSet();
		if (cs instanceof RestMethodControllerSet4Invoker || cs instanceof RmethodControllerSet4Invoker) {
			IServiceInvoker si = ServiceInvokerSet.instance.getServiceInvoker(cm.getServicePath());
			return si.getObjectCallHelper().getObject();
		} else
			return null;
	}
	
	public static Map<String,Object> getObjectsMap(){
		Set<String> paths = ServiceInvokerSet.instance.getAcceptPaths();

		Hashtable<String, Object> ret = new Hashtable<String, Object>();
		for (String path:paths){
			ret.put(path, ServiceInvokerSet.instance.getServiceInvoker(path).getObjectCallHelper().getObject());
		}
		return ret;
//
//		
//		
//		Hashtable<String, Object> ret = new Hashtable<String, Object>();
//		Map<String, Object> map = PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_RESTMETHOD);
//		for (String path:map.keySet()){
//			ret.put(path, getObject(path));
//		}
//		
//		map = PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_REMOTECALL);
//		for (String path:map.keySet()){
//			ret.put(path, getObject(path));
//		}
//		return ret;
	}
}
