package net.jplugin.ext.webasic.impl.rmethod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.api.IController;
import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvokerSet;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 下午02:02:05
 **/

public class RmethodControllerSet4Invoker implements IControllerSet{

	
	private Map<String, IController> controllerMap;

	Set<String> servicePathSet = new HashSet();
	public void init() {
		Map<String, ObjectDefine> defs = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL,ObjectDefine.class);
		servicePathSet.addAll(defs.keySet());
		ServiceInvokerSet.instance.addServices(defs);
		
//		Map<String, ObjectDefine> defs = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL,ObjectDefine.class);
//		
//		controllerMap = new ConcurrentHashMap<String, IController>();
//		
//		for (Entry<String, ObjectDefine> en:defs.entrySet()){
//			controllerMap.put(en.getKey(), new RmethodController(en.getValue()));
//		}
	}
	
	public Set<String> getAcceptPaths() {
		return servicePathSet;
//		return controllerMap.keySet();
	}

	public void dohttp(String path,HttpServletRequest req, HttpServletResponse res,String innerPath)
			throws Throwable {
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(CallParam.REMOTE_PARATYPES_KEY, req.getParameter(CallParam.REMOTE_PARATYPES_KEY));
		map.put(CallParam.REMOTE_PARAVALUES_KEY, req.getParameter(CallParam.REMOTE_PARAVALUES_KEY));
		CallParam cp = CallParam.create(CallParam.CALLTYPE_REMOTE_CALL,path, innerPath, map);

//		controllerMap.get(path).dohttp(req, res,innerPath);
		
		ServiceInvokerSet.instance.call(cp);
		String result = cp.getResult();
		res.getWriter().print(result);
	}
	
//	public RmethodController getRMethodController(String path){
//		return (RmethodController) controllerMap.get(path);
//	}

}
