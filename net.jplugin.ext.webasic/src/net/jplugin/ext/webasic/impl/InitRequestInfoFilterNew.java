package net.jplugin.ext.webasic.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.RequestIdKit;
import net.jplugin.common.kits.http.ContentKit;
import net.jplugin.core.kernel.api.ctx.Cookies;
import net.jplugin.core.kernel.api.ctx.Headers;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.RequesterInfo.Content;
import net.jplugin.core.kernel.kits.KernelKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.webasic.api.WebFilter;
import net.jplugin.ext.webasic.kits.StreamContentKit;

/**
 * This filter must not conflict with InitRequestInfoFilter。This is two seperate
 * method。
 * 
 * @author Administrator
 *
 */
public class InitRequestInfoFilterNew implements WebFilter {

	//放在request中的
	private static final String _AID = "_aid";
	private static final String _OID = "_oid";
	private static final String _OTK = "_otk";
	private static final String _ATK = "_atk";

	//放在head中的
	private static final String HAID = "HAID";
	private static final String HOID = "HOID";
	private static final String HOTK = "HOTK";
	private static final String HATK = "HATK";
	
	
	public static final String _REQID = "ReqSerialKey";

	public boolean doFilter(HttpServletRequest req, HttpServletResponse res) {
//		try {
//			req.setCharacterEncoding("utf-8");
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException(e);
//		}
		//PUT http servlet reqeust to context
		ThreadLocalContext tlContext = ThreadLocalContextManager.instance.getContext();
		tlContext.setAttribute(ThreadLocalContext.ATTR_SERVLET_REQUEST, req);
		
		RequesterInfo requestInfo = tlContext.getRequesterInfo();
		
		//parse content
		parseContent(requestInfo, req);
		//parse cookies
		parseCookies(requestInfo,req);
		//parse headers
		parseHeaders(requestInfo,req);
		
		//fill request url
		requestInfo.setRequestUrl(req.getRequestURL().toString());
		//fill client ip
		requestInfo.setCallerIpAddress(getClientIp(req));
		
		//fill other attribute from content,cookie,header
		fillFromBasicReqInfo(requestInfo);
		
		return true;
	}

	private void parseHeaders(RequesterInfo requestInfo, HttpServletRequest req) {
		//存在大小写问题，暂时修改方案
//		Enumeration<String> headers = req.getHeaderNames();
//		if (headers!=null)
//			while(headers.hasMoreElements()){
//				String h = headers.nextElement();
//				requestInfo.getHeaders().setHeader(h, req.getHeader(h));
//			}

		
		requestInfo.getHeaders().setHeader(_REQID, req.getHeader(_REQID));
		requestInfo.getHeaders().setHeader("Referer", req.getHeader("Referer"));
		requestInfo.getHeaders().setHeader(MtInvocationFilterHandler.TENANT_ID, req.getHeader(MtInvocationFilterHandler.TENANT_ID));

		requestInfo.getHeaders().setHeader(HAID, req.getHeader(HAID));
		requestInfo.getHeaders().setHeader(HOID, req.getHeader(HOID));
		requestInfo.getHeaders().setHeader(HOTK, req.getHeader(HOTK));
		requestInfo.getHeaders().setHeader(HATK, req.getHeader(HATK));
	}

	private void parseCookies(RequesterInfo requestInfo, HttpServletRequest req) {
		//fill cookie
		Cookie[] arr = req.getCookies();
		if (arr!=null){
			Cookies cookies = requestInfo.getCookies();
			for (Cookie c:arr){
				cookies.setCookie(c.getName(), c.getValue());
			}
		}
	}
	
	public static void fillFromBasicReqInfo(RequesterInfo requestInfo) {
		Content content = requestInfo.getContent();
		
		Map map = content.getParamContent();

		String clientAppToken = (String) map.get(_ATK);
		String operatorToken = (String) map.get(_OTK);
		//如此判断，避免和InitRequestInfoFilter冲突了
		if (clientAppToken!=null  || operatorToken!=null){
			requestInfo.setClientAppToken(clientAppToken);
			requestInfo.setOperatorToken(operatorToken);
			requestInfo.setOperatorId((String) map.get(_OID));
			requestInfo.setClientAppCode((String) map.get(_AID));
		}else{
			//2018-3-18 增加支持Header中信息
			Headers headers = requestInfo.getHeaders();
			clientAppToken = headers.getHeader(HATK);
			operatorToken = headers.getHeader(HOTK);
			if (clientAppToken!=null  || operatorToken!=null){
				requestInfo.setClientAppToken(clientAppToken);
				requestInfo.setOperatorToken(operatorToken);
				requestInfo.setOperatorId( headers.getHeader(HOID));
				requestInfo.setClientAppCode( headers.getHeader(HAID));
			}	
		}
		
		//设置tenant
		MtInvocationFilterHandler.instance.handle(requestInfo);
	}

	private String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if(ip!=null && ip.length()>15){ //"***.***.***.***".length() = 15
			if(ip.indexOf(",")>0){
				ip = ip.substring(0,ip.indexOf(","));
			}
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	private void parseContent(RequesterInfo reqInfo, HttpServletRequest req) {
		Content content = reqInfo.getContent();
		String theContentType = req.getContentType();
		content.setContentType(theContentType);
		if (ContentKit.isApplicationJson(theContentType)) {//用ContentKit,startwith判断，支持 application/json;encoding... 的样式
			String json = StreamContentKit.readReqStream(req);
			content.setJsonContent(json);
		} else {
			Enumeration nms = req.getParameterNames();
			HashMap<String, String> param = new HashMap<String, String>();
			while (nms.hasMoreElements()) {
				String name = (String) nms.nextElement();
				param.put(name, req.getParameter(name));
			}
			content.setParamContent(param);
		}
	}



	public void doAfter(HttpServletRequest req, HttpServletResponse res, Throwable th) {
	}

}
