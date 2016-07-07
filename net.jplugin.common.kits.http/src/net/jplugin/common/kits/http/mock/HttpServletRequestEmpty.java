package net.jplugin.common.kits.http.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 上午11:54:34
 **/

public class HttpServletRequestEmpty implements HttpServletRequest{

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	public Cookie[] getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	public long getDateHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
	 */
	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	public Enumeration getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	public Enumeration getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	public int getIntHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	public String getMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	public String getRequestURI() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	public HttpSession getSession(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
	 */
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
	 */
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
	 */
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
	 */
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
	 */
	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	public int getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getInputStream()
	 */
	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalAddr()
	 */
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalName()
	 */
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalPort()
	 */
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocale()
	 */
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocales()
	 */
	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	public Enumeration getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getProtocol()
	 */
	public String getProtocol() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getReader()
	 */
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
	 */
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemoteAddr()
	 */
	public String getRemoteAddr() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemoteHost()
	 */
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemotePort()
	 */
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getScheme()
	 */
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServerName()
	 */
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServerPort()
	 */
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#isSecure()
	 */
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
	}

	public AsyncContext getAsyncContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public DispatcherType getDispatcherType() {
		// TODO Auto-generated method stub
		return null;
	}

	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	public AsyncContext startAsync() {
		// TODO Auto-generated method stub
		return null;
	}

	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean authenticate(HttpServletResponse arg0) throws IOException,
			ServletException {
		// TODO Auto-generated method stub
		return false;
	}

	public Part getPart(String arg0) throws IOException, IllegalStateException,
			ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Part> getParts() throws IOException,
			IllegalStateException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	public void login(String arg0, String arg1) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void logout() throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public long getContentLengthLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String changeSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T extends HttpUpgradeHandler> T upgrade(Class<T> arg0)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

}
