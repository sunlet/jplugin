package net.jplugin.ext.webasic.impl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.webasic.Plugin;
import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.api.WebContext;
import net.jplugin.ext.webasic.api.WebFilter;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-2 下午05:57:36
 **/

public class WebDriver {
	public static WebDriver INSTANCE = new WebDriver();
	public static final String SERVICE_CALL = "/service";
//	public static final String OPERATION_KEY = "_o";
	private static Logger logger;
	IControllerSet[] controllerSets;
	ConcurrentHashMap<String, IControllerSet> pathMap=new ConcurrentHashMap<String, IControllerSet>();
	
	private WebFilter[] filters;
	/**
	 * @param extensionMap
	 * @param webfilters 
	 * @param controllerMap2 
	 */
	public void init() {
		controllerSets = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_CONTROLLERSET,IControllerSet.class);
		filters = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_WEBFILTER,WebFilter.class);
		
		for ( int i=0;i<controllerSets.length;i++){
			controllerSets[i].init();
		}
		
		//初始化pathmap
		for (IControllerSet is:controllerSets){
			Set<String> paths = is.getAcceptPaths();
			for (String p:paths){
				if (pathMap.containsKey(p)){
					throw new RuntimeException("Multi web handlers can work with :"+p);
				}
				pathMap.put(p, is);
			}
		}
		
	}

	public void dohttp(HttpServletRequest req,HttpServletResponse res){
		ThreadLocalContext tlc =null;
		try{
			tlc = ThreadLocalContextManager.instance.createContext();
			
			WebContext.initFromRequest(req);
			if (doWebFilter(req,res)){
				dohttpThrowEx(req, res);
			}
		}catch(Throwable e){
			e.printStackTrace();
			getLogger().error("Error when service "+req.getRequestURI(),e);
		}finally{
			ThreadLocalContextManager.instance.releaseContext();
		}
	}
	
	/**
	 * 如果有返回false，则中断
	 * @param req
	 * @param res
	 * @return
	 */
	private boolean doWebFilter(HttpServletRequest req, HttpServletResponse res) {
		for (WebFilter wf:filters){
			if (!wf.doFilter(req, res)){
				return false;
			}
		}
		return true;
	}

	/**
	 * @return 
	 * 
	 */
	private Logger getLogger() {
		if (logger == null){
			synchronized (this.getClass()) {
				ILogService log = ServiceFactory.getService(ILogService.class);
				logger = log.getLogger(this.getClass().getName());
			}
		}
		return logger;
	}

	
	private void dohttpThrowEx(HttpServletRequest req,HttpServletResponse res) throws Throwable{
		String path = req.getServletPath();
		
		//出去点
		int dotPos = path.lastIndexOf('.');
		if ( dotPos >= 0){
			path = path.substring(0,dotPos);
		}
		
		IControllerSet ctroller = pathMap.get(path);

		if (ctroller!=null){
			ctroller.dohttp(path,req, res,null);
			return;
		}

		int splitPos = path.lastIndexOf('/');
		//等于0的时候不适合，只有一个path
		if (splitPos>0){
			String prePath = path.substring(0, splitPos);
			String postPath = path.substring(splitPos+1);
			ctroller = pathMap.get(prePath);
			if (ctroller!=null){
				ctroller.dohttp(prePath,req,res,postPath);
				return;
			}
		}
			
		throw new RuntimeException("Can't find controller for :"+path);
		
	}
}
