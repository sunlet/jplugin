package net.jplugin.ext.webasic.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jplugin.core.ctx.api.RuleProxyHelper;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.api.IDynamicService;
import net.jplugin.ext.webasic.api.InvocationContext;
import net.jplugin.ext.webasic.impl.WebDriver.ControllerMeta;
import net.jplugin.ext.webasic.impl.filter.IMethodCallback;
import net.jplugin.ext.webasic.impl.filter.service.ServiceFilterManager;
import net.jplugin.ext.webasic.impl.restm.RestMethodControllerSet4Invoker;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;
import net.jplugin.ext.webasic.impl.restm.invoker.IServiceInvoker;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvokerSet;
import net.jplugin.ext.webasic.impl.rmethod.RmethodControllerSet4Invoker;
import net.jplugin.ext.webasic.impl.web.WebController;
import net.jplugin.ext.webasic.impl.web.WebControllerSet;

public class ESFHelper {
	
//	/**
//	 * RPC调用这个方法
//	 * @param obj
//	 * @param method
//	 * @param args
//	 * @return
//	 * @throws Throwable
//	 */
//	@Deprecated
//	public static Object invokeWithRule(String servicePath,final Object obj, final Method method, final Object[] args) throws Throwable{
//		try{
//			if (obj instanceof IDynamicService) 
//				throw new RuntimeException("Dynamic implemented service, not support rpc invoke. "+servicePath);
//			
//			ThreadLocalContextManager.instance.createContext();
//			InvocationContext sfc = new InvocationContext(servicePath, obj, method, args);
//			
//			return ServiceFilterManager.INSTANCE.executeWithFilter(sfc,new IMethodCallback() {
//				public Object run() throws Throwable {
//					return RuleProxyHelper.invokeWithRule(obj, method, args);
//				}
//			});
//		}finally{
//			ThreadLocalContextManager.instance.releaseContext();
//		}
//	}

	public static Object invokeWithRule(ESFRPCContext ctx,String servicePath,final Object obj, final Method method, final Object[] args) throws Throwable{
		
		if (obj instanceof IDynamicService) 
			throw new RuntimeException("Dynamic implemented service, not support rpc invoke. "+servicePath);
		try{
			ThreadLocalContextManager.instance.createContext();
			ESFRPCContext.fill(ctx);
			
			InvocationContext sfc = new InvocationContext(servicePath, obj, method, args);
			
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
	public static void callRestfulService(ESFRestContext ctx,CallParam cp)  throws Throwable{
		try{
			ThreadLocalContextManager.instance.createContext();
			//fill content
			ESFRestContextHelper.fillContentForRestful(cp,ctx);
			
			//fill other attribute
			InitRequestInfoFilterNew.fillFromBasicReqInfo(ThreadLocalContextManager.getRequestInfo());
			
			//call the service
			ServiceInvokerSet.instance.call(cp);
		}finally{
			ThreadLocalContextManager.instance.releaseContext();
		}
	}
//	@Deprecated
//	public static void callRestfulService(CallParam cp)  throws Throwable{
//		try{
//			ThreadLocalContextManager.instance.createContext();
//			//fill content
//			ESFRestContextHelper.fillContentForRestful(cp);
//			//fill ipaddress
////			ESFRestContext.fill(ctx);
//			//fill other attribute
//			InitRequestInfoFilterNew.fillFromBasicReqInfo(ThreadLocalContextManager.getRequestInfo());
//			
//			//call the service
//			ServiceInvokerSet.instance.call(cp);
//		}finally{
//			ThreadLocalContextManager.instance.releaseContext();
//		}
//	}
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
	
	/**
	 * 特别强调：由于WebExController在每次执行都需要创建一个新的，所有不能获取到静态的列表。
	 * @return
	 */
	public static List<Object> getWebControllerObjects(){
		IControllerSet[] css = WebDriver.INSTANCE.getControllerSet();
		List<Object> result = new ArrayList();
		for (IControllerSet cs:css){
			if (cs instanceof WebControllerSet){
				WebControllerSet wcs = (WebControllerSet) cs;
				for (WebController o:wcs.getControllerMap().values()){
					result.add(o.getObject());
				}
			}
		}
		return result;
	}
}
