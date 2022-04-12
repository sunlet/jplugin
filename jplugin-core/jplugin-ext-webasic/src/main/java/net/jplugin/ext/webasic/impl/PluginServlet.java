package net.jplugin.ext.webasic.impl;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.ExceptionKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-3 上午10:21:05
 **/

public class PluginServlet extends HttpServlet{

	/**
	 * <PRE>
	 * 浏览器跨域访问，对于非简单类型请求，会首先调用OPTIONS请求来预检。
	 * 这个方法来支持预检。设置一个有效期18000ms，减少预检的频度。
	 * </PRE>
	 */
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String accessControlAllowOrigin = ConfigFactory.getStringConfigWithTrim("platform.access-control-allow-origin");
		if (StringKit.isNotNull(accessControlAllowOrigin)) {
			resp.setHeader("Access-Control-Allow-Origin",accessControlAllowOrigin);
			resp.setHeader("Access-Control-Allow-Headers","content-type");
			resp.setHeader("Allow","GET, HEAD, POST");
			resp.setHeader("Access-Control-Max-Age", "18000");
		}else {
			super.doOptions(req, resp);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dohttp(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dohttp(req, resp);
	}
	
	public void dohttp(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
//		ThreadLocalContext tlc =null;
//		try{
//			tlc = ThreadLocalContextManager.instance.createContext();
//			WebDriver.INSTANCE.dohttp(req, res);
//		}finally{
//			ThreadLocalContextManager.instance.releaseContext();
//		}
		WebDriver.INSTANCE.dohttp(req, res);
	}
	

	@Override
	public void init() throws ServletException {
		if (embedMode) {
			PluginEnvirement.INSTANCE.getStartLogger().log("Servlet init ignored, You may start tomcat in embed tomcat state...");
			return;
		}
		initcalled = true;
			
		PluginEnvirement.INSTANCE.getStartLogger().log("Servlet init...");
		try{
			//set the work directory 
			if (StringKit.isNull(System.getProperty(PluginEnvirement.WORK_DIR))){
				String catalinaHome = System.getProperty("catalina.home");
				if (StringKit.isNull(catalinaHome))
					catalinaHome = new File(".").getAbsolutePath();
				String workdir = catalinaHome+"/logs/"+"jplugin-work";
				System.setProperty(PluginEnvirement.WORK_DIR,workdir );
			}
			//append context path
			String contextPath = this.getServletContext().getContextPath();
			if ("/".equals(contextPath))
				contextPath = "/_ROOT";
			PluginEnvirement.getInstance().setWorkDir(System.getProperty(PluginEnvirement.WORK_DIR)+contextPath);
//			System.setProperty(PluginEnvirement.WORK_DIR,System.getProperty(PluginEnvirement.WORK_DIR)+contextPath);
			
			//make dir if not exists
			FileKit.makeDirectory(PluginEnvirement.getInstance().getWorkDir());
			PluginEnvirement.getInstance().startup();
			PluginEnvirement.getInstance().setWebRootPath(getServletContext().getRealPath("/"));
		}catch(Throwable t){
			t = ExceptionKit.getRootCause(t);
			PluginEnvirement.getInstance().getStartLogger().log(t.getMessage(),t);;
			if (t instanceof RuntimeException) throw (RuntimeException)t;
			else throw new RuntimeException(t);
		}
	}
	
	private static boolean initcalled = false;
	private static boolean embedMode=false;

	public static void checkAndUseEmbedMode() {
		/**
		 * 确定是Servlet容器拉起来的,  然后设置模式为Embed模式,这样再init的时候就不会执行实质内容了! 
		 */
		if (initcalled) {
			throw new RuntimeException("Jplugin is running in ServletContainer , You must remove the jplugin-embed-tomcat dependency!");
		}else {
			embedMode = true;
		}

	}

	@Override
	public void destroy() {
		PluginEnvirement.getInstance().stop();
	}

	
}
