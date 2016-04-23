package net.jplugin.ext.webasic.impl.restm;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;
import net.jplugin.ext.webasic.impl.restm.invoker.IServiceInvoker;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvoker;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvokerSet;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 下午02:02:12
 **/

public class RestMethodControllerSet4Invoker implements IControllerSet{

	Set<String> servicePathSet = new HashSet();
	public void init() {
		Map<String, ObjectDefine> defs = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD,ObjectDefine.class);
		servicePathSet.addAll(defs.keySet());
		ServiceInvokerSet.instance.addServices(defs);
	}
	
	public Set<String> getAcceptPaths() {
		return servicePathSet;
//		return ServiceInvokerSet.instance.getAcceptPaths();
	}

	public void dohttp(String path,HttpServletRequest req, HttpServletResponse res,String innerPath)
			throws Throwable {
		Enumeration<String> names = req.getParameterNames();
		String name = null;
		Map<String,String> reqMap = new HashMap<String, String>();
		while (names.hasMoreElements()){
			name = names.nextElement();
			reqMap.put(name, req.getParameter(name));
		}
		CallParam callParam = CallParam.create(path,innerPath, reqMap);
		ServiceInvokerSet.instance.call(callParam);
		String result = callParam.getResult();
		
		res.setCharacterEncoding("utf-8");
		res.setContentType("text/json");
		res.getWriter().print(result);
		//controllerMap.get(path).dohttp(req, res,innerPath);
	}
}
