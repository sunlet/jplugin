package net.jplugin.ext.webasic.impl;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.FileKit;
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
	
	public void dohttp(HttpServletRequest req,HttpServletResponse res){
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
			System.setProperty(PluginEnvirement.WORK_DIR, System.getProperty("user.dir")+"/../nswork");
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
