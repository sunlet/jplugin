package net.jplugin.ext.webasic.impl.restm.invoker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.core.kernel.api.Beans;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.api.IController;
import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.impl.helper.ObjectCallHelper;


/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 下午02:02:12
 **/

public class ServiceInvokerSet implements IServiceInvokerSet{
	
	public static IServiceInvokerSet instance = new ServiceInvokerSet();

	private Map<String, IServiceInvoker> serviceMap;

	private ServiceInvokerSet(){}
	
//	public void init() {
//		Map<String, ObjectDefine> defs = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD,ObjectDefine.class);
//		serviceMap = new HashMap<String, IServiceInvoker>();
//		
//		for (Entry<String, ObjectDefine> en:defs.entrySet()){
//			serviceMap.put(en.getKey(), new ServiceInvoker(en.getValue()));
//		}
//	}
	public Set<String>  getPathSet(){
		return serviceMap.keySet();
	}

	public void addServices(Map<String, ObjectDefine> defs) {
		if (serviceMap==null)
			serviceMap = new HashMap<String, IServiceInvoker>();
		
		for (Entry<String, ObjectDefine> en:defs.entrySet()){
			if (serviceMap.get(en.getKey())!=null)
				throw new RuntimeException("duplicate service path:"+en.getKey());
			
			IServiceInvoker invoker = new ServiceInvoker(en.getValue());
			serviceMap.put(en.getKey(), invoker);
			
			//重新设置value值
			Beans.resetValue(en.getValue(), invoker.getObjectCallHelper().getObject());
		}
	}
	
	public Set<String> getAcceptPaths() {
		return serviceMap.keySet();
	}
	
	public void call(CallParam cp)  throws Throwable{
		IServiceInvoker s = serviceMap.get(cp.getPath());
		if (s==null) 
			throw new RuntimeException("Can't find the service by path : "+cp.getPath());
		s.call(cp);
	}
	
	/**
	 * 为了支持ESF调用引入
	 * @param path
	 * @return
	 */
	public IServiceInvoker getServiceInvoker(String path){
		return serviceMap.get(path);
	}

}
