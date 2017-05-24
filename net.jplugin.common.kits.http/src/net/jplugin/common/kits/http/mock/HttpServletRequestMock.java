package net.jplugin.common.kits.http.mock;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import net.jplugin.common.kits.AssertKit;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 上午11:53:08
 **/

public class HttpServletRequestMock extends HttpServletRequestEmpty{
	HttpServletResponseMock res;
	
	static String URL_BASE = "http://localhost:8080";
	static String CTX_PATH="/demo";

	public void setResponse(HttpServletResponseMock res){
		this.res = res;
	}
	public void setSession(HttpSessionMock sessMock){
		this.sessionMock = sessMock;
	}

	
	public class TheRequestDispatcher implements RequestDispatcher {

		public void forward(ServletRequest arg0, ServletResponse arg1)
				throws ServletException, IOException {
			//直接把结果输出
			for (String key:attrMap.keySet()){
				res.getWriter().print(attrMap.get(key));
			}
		}

		public void include(ServletRequest arg0, ServletResponse arg1)
				throws ServletException, IOException {
		}
	}

	HashMap<String, String> paraMap = new HashMap<String, String>();
	HashMap<String, Object> attrMap = new HashMap<String, Object>();
	private String serveletPath;
	private HttpSession sessionMock;
	@Override
	public String getParameter(String name) {
		return paraMap.get(name);
	}
	
	public HttpServletRequestMock setPara(String name,String value){
		paraMap.put(name, value);
		return this;
	}
	@Override
	public Object getAttribute(String attr) {
		return attrMap.get(attr);
	}
	

	public RequestDispatcher getRequestDispatcher(String arg0) {
		return new TheRequestDispatcher();
	}
	
	/* (non-Javadoc)
	 * @see net.luis.plugin.webasic.api.mock.HttpServletRequestEmpty#getParameterMap()
	 */
	@Override
	public Map getParameterMap() {
		HashMap map = new HashMap();
		for (String key:this.paraMap.keySet()){
			map.put(key, new String[]{this.paraMap.get(key)});
		}
		return map;
	}
	
	@Override
	public String getServletPath() {
		return serveletPath;
	}
	
	public HttpServletRequestMock setServletPath(String path){
		this.serveletPath = path;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String key, Object v) {
		this.attrMap.put(key, v);
	}
	
	
	@Override
	public HttpSession getSession() {
		return sessionMock;
	}
	
	
	@Override
	public String getContextPath() {
		return CTX_PATH;
	}
	
	@Override
	public StringBuffer getRequestURL() {
		return new StringBuffer(URL_BASE+this.CTX_PATH+this.getServletPath());
	}
	
	@Override
	public int getLocalPort() {
		return 8080;
	}
	@Override
	public String getRemoteAddr() {
		return "127.0.0.1";
	}
	
	@Override
	public Enumeration getParameterNames() {
		final Iterator it = getParameterMap().keySet().iterator();

		return new Enumeration<String>() {
			public boolean hasMoreElements() {
				return it.hasNext();
			}

			public String nextElement() {
				return (String) it.next();
			}
		};
	}
	
	
	public void initFromUrl(String url) {
		if (url.indexOf('?')>=0) throw new RuntimeException("not support get method now");
		AssertKit.assertTrue(url.startsWith(URL_BASE + CTX_PATH));
		this.serveletPath = url.substring(URL_BASE.length()+CTX_PATH.length());
	}
	public void putAllParameter(Map<String, Object> datas) {
		for (String k:datas.keySet()){
			this.paraMap.put(k,(String) datas.get(k));
		}
	}
}
