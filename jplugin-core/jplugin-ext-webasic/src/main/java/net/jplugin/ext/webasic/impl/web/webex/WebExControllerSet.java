package net.jplugin.ext.webasic.impl.web.webex;

import net.jplugin.core.kernel.api.ExtensionObjects;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.api.IController;
import net.jplugin.ext.webasic.api.IControllerSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 下午02:02:05
 **/

public class WebExControllerSet implements IControllerSet{
	private Map<String, IController> controllerMap;

//	public void init() {
//		Map<String, ClassDefine> defs = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_WEBEXCONTROLLER,ClassDefine.class);
//
//		controllerMap = new ConcurrentHashMap<String, IController>();
//
//		for (Entry<String, ClassDefine> en:defs.entrySet()){
//			WebExController exController = new WebExController(en.getValue());
//			controllerMap.put(en.getKey(), exController);
//
//			//重新设置value值
//			Beans.resetValue(en.getValue(), exController.getObject());
//		}
//	}
	public void init() {
		Map<String, Object> defs = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_WEBEXCONTROLLER);

		controllerMap = new ConcurrentHashMap<String, IController>();

		for (Entry<String, Object> en:defs.entrySet()){
			WebExController exController = new WebExController(en.getValue());
			controllerMap.put(en.getKey(), exController);

			//重新设置value值
//			ExtensionObjects.resetValue(en.getValue(), exController.getObject());
		}
	}
	public Set<String> getAcceptPaths() {
		return controllerMap.keySet();
	}

	public void dohttp(String path,HttpServletRequest req, HttpServletResponse res,String innerPath)
			throws Throwable {
		controllerMap.get(path).dohttp(path,req, res,innerPath);
	}
	

}
