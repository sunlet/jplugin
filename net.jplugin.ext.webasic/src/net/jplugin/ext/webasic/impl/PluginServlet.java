package net.jplugin.ext.webasic.impl;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-3 上午10:21:05
 **/

public class PluginServlet extends HttpServlet{

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
		System.out.println("Servlet init...");
		try{
			//set the work directory 
			if (StringKit.isNull(System.getProperty(PluginEnvirement.WORK_DIR))){
				String catalinaHome = System.getProperty("catalina.home");
				if (StringKit.isNull(catalinaHome))
					catalinaHome = new File(".").getAbsolutePath();
				String workdir = catalinaHome+"/"+"jplugin-work";
				System.setProperty(PluginEnvirement.WORK_DIR,workdir );
			}
			//append context path
			String contextPath = this.getServletContext().getContextPath();
			if ("/".equals(contextPath))
				contextPath = "/_ROOT";
			System.setProperty(PluginEnvirement.WORK_DIR,System.getProperty(PluginEnvirement.WORK_DIR)+contextPath);
			
			//make dir if not exists
			FileKit.makeDirectory(PluginEnvirement.getInstance().getWorkDir());
			PluginEnvirement.getInstance().startup();
			PluginEnvirement.getInstance().setWebRootPath(getServletContext().getRealPath("/"));
		}catch(Throwable t){
			t.printStackTrace();
			if (t instanceof RuntimeException) throw (RuntimeException)t;
			else throw new RuntimeException(t);
		}
	}
}
