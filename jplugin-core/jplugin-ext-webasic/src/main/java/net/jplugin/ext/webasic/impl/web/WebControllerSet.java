package net.jplugin.ext.webasic.impl.web;

import net.jplugin.core.kernel.api.ExtensionObjects;
import net.jplugin.core.kernel.api.PluginEnvirement;
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

public class WebControllerSet implements IControllerSet{
	private Map<String, WebController> controllerMap;

	public void init() {
//		Map<String, ObjectDefine> defs = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER,ObjectDefine.class);
		Map<String, Object> defs = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER,Object.class);
		
		controllerMap = new ConcurrentHashMap<String, WebController>();
		
//		for (Entry<String, ObjectDefine> en:defs.entrySet()){
		for (Entry<String, Object> en:defs.entrySet()){
			WebController controller = new WebController(en.getValue()) ;
			controllerMap.put(en.getKey(), controller);
			
			//重新设置value值
//			ExtensionObjects.resetValue(en.getValue(), controller.getObject());
		}
	}
	
	public Map<String,WebController> getControllerMap(){
		return this.controllerMap;
	}
	
	public Set<String> getAcceptPaths() {
		return controllerMap.keySet();
	}

	public void dohttp(String path,HttpServletRequest req, HttpServletResponse res,String innerPath)
			throws Throwable {
		controllerMap.get(path).dohttp(path,req, res,innerPath);
	}
	

}
