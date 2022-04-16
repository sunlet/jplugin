package net.jplugin.ext.webasic.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.ctx.api.RuleProxyHelper;
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.PluginFilterManager;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.ext.webasic.Plugin;
import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.api.IDynamicService;
import net.jplugin.ext.webasic.api.InvocationContext;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.impl.WebDriver.ControllerMeta;
import net.jplugin.ext.webasic.impl.filter.IMethodCallback;
import net.jplugin.ext.webasic.impl.filter.service.ServiceFilterManager;
import net.jplugin.ext.webasic.impl.restm.RestMethodControllerSet4Invoker;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;
import net.jplugin.ext.webasic.impl.restm.invoker.IServiceInvoker;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvokerSet;

public class ESFHelper {
	public static void init(){
		rpcFilterManager.init();
		restFilterManager.init();
	}
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

	private static  int SERVICE_TIME_LIMIT = 6000;
	static{
		SERVICE_TIME_LIMIT = ConfigFactory.getIntConfig("platform.service-time-limit",6000);
		PluginEnvirement.getInstance().getStartLogger().log("$$$ platform.service-time-limit is "+SERVICE_TIME_LIMIT);
	}
	
	static PluginFilterManager<Tuple2<ESFRPCContext, InvocationContext>> rpcFilterManager = new PluginFilterManager<>(Plugin.EP_ESF_RPC_FILTER, 
			(fc,ctx)->{
				ESFRPCContext.fill(ctx.first);
				return ServiceFilterManager.INSTANCE.executeWithFilter(ctx.second,new IMethodCallback() {
					public Object run() throws Throwable {
						return RuleProxyHelper.invokeWithRule(ctx.second.getObject(), ctx.second.getMethod(), ctx.second.getArgs());
					}
				});
			});
	
	public static Object invokeWithRule(ESFRPCContext ctx,String servicePath,final Object obj, final Method method, final Object[] args) throws Throwable{
		checkTimeOut(ctx.getMsgReceiveTime());
		if (obj instanceof IDynamicService) 
			throw new RuntimeException("Dynamic implemented service, not support rpc invoke. "+servicePath);
		try{
			ThreadLocalContextManager.instance.createContext();
			InvocationContext sfc = new InvocationContext(servicePath, obj, method, args);
			
			return rpcFilterManager.filter(Tuple2.with(ctx,sfc));
//			ESFRPCContext.fill(ctx);
//			return ServiceFilterManager.INSTANCE.executeWithFilter(sfc,new IMethodCallback() {
//				public Object run() throws Throwable {
//					return RuleProxyHelper.invokeWithRule(obj, method, args);
//				}
//			});
		}finally{
			ThreadLocalContextManager.instance.releaseContext();
		}
	}

	private static void checkTimeOut(long msgReceiveTime) {
		if (msgReceiveTime>0 && (System.currentTimeMillis()-msgReceiveTime) > SERVICE_TIME_LIMIT){
			throw new RemoteExecuteException("1005","执行超时. limit="+SERVICE_TIME_LIMIT);
		}
	}
	
	static PluginFilterManager<Tuple2<ESFRestContext,CallParam>> restFilterManager = new PluginFilterManager<>(Plugin.EP_ESF_REST_FILTER,
			(fc,ctx)->{
				//fill content
				ESFRestContextHelper.fillContentForRestful(ctx.second,ctx.first);
				//fill other attribute
				InitRequestInfoFilterNew.fillFromBasicReqInfo(ThreadLocalContextManager.getRequestInfo());
				//call the service
				ServiceInvokerSet.instance.call(ctx.second);
				return null;
			});
	
	/**
	 * Restful调用这个方法
	 * @param cp
	 * @throws Throwable
	 */
	public static void callRestfulService(ESFRestContext ctx,CallParam cp)  throws Throwable{
		checkTimeOut(ctx.getMsgReceiveTime());
		try{
			ThreadLocalContextManager.instance.createContext();
			restFilterManager.filter(Tuple2.with(ctx,cp));
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
//		if (cs instanceof RestMethodControllerSet4Invoker || cs instanceof RmethodControllerSet4Invoker) {
		if (cs instanceof RestMethodControllerSet4Invoker) {
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
	 * 返回 Web控制器的注册列表
	 */
	public static Map<String,Class> getWebControllerClasses(){
		Map<String, ObjectDefine> objects = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER,ObjectDefine.class);
		Map<String, ClassDefine> clazzes = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_WEBEXCONTROLLER,ClassDefine.class);
		
		Map<String,Class> ret = new HashMap<>();
		
		for (Entry<String, ObjectDefine> en:objects.entrySet()){
			ret.put(en.getKey(), en.getValue().getObjClass());
		}
		for (Entry<String, ClassDefine> en:clazzes.entrySet()){
			ret.put(en.getKey(), en.getValue().getClazz());
		}
		return ret;
		
//		IControllerSet[] css = WebDriver.INSTANCE.getControllerSet();
//		List<Object> result = new ArrayList();
//		for (IControllerSet cs:css){
//			if (cs instanceof WebControllerSet){
//				WebControllerSet wcs = (WebControllerSet) cs;
//				for (WebController o:wcs.getControllerMap().values()){
//					result.add(o.getObject());
//				}
//			}
//		}
//		return result;
	}
}
