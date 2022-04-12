package net.jplugin.ext.webasic.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;

public class RequestFilter implements Filter {

	private Logger log;

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain fc) throws IOException, ServletException {
		//dispose head pic
		HttpServletRequest threq = (HttpServletRequest) req;
		String uri = threq.getServletPath();
		if (uri.endsWith(".html")){
			HttpServletResponse hres = (HttpServletResponse) res;
			hres.addHeader("Access-Control-Allow-Origin","*");	
			String realpath = req.getServletContext().getRealPath(uri);
			File file = new File(realpath);
			try {
				hres.setContentType("text/html; charset=utf-8");
				hres.getWriter().print(FileKit.file2String(realpath,"utf-8"));
				return;
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		//end of dispose
		if (log == null) {
			synchronized (this) {
				if (log == null) {
					ILogService logger = ServiceFactory
							.getService(ILogService.class);
					log = logger.getSpecicalLogger("request.log");
				}
			}
		}
		HttpServletRequest hreq = (HttpServletRequest) req;
		long start = System.currentTimeMillis();
		fc.doFilter(req, res);
		long end = System.currentTimeMillis();
		
		log.info(CalenderKit.getCurrentTimeString()+" "+" dural:["+(end-start) +"] "+hreq.getRequestURL().toString());
		
		
		
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
