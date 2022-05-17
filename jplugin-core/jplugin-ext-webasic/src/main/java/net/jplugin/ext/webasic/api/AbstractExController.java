package net.jplugin.ext.webasic.api;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.MD5Kit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.ctx.api.JsonResult;
import net.jplugin.core.ctx.api.RuleResult;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.webasic.kits.StreamContentKit;

public class AbstractExController {
	private static final String WA_CK = "wa_ck";
	private static final String WA_MD5 = "wa_md5";
	private static final String WA_CH = "wa_ch";
	public static final String JSP_BASE = "/WEB-INF/classes/";
	private static final String HTTP_REQ = "HTTP_REQ";
	private static final String HTTP_RES = "HTTP_RES";

//	private HttpServletRequest req;
//	private HttpServletResponse res;

	private static Boolean autoSetParamToReqAttr=null;//默认值为true，保持兼容
	static{
		autoSetParamToReqAttr = !"false".equalsIgnoreCase(ConfigFactory.getStringConfigWithTrim("platform.abs-exctl-auto-set-param-to-reqattr"));
		PluginEnvirement.getInstance().getStartLogger().log("autoSetParamToReqAttr="+autoSetParamToReqAttr);
	}
	
	public final void _init(HttpServletRequest req, HttpServletResponse res) {
		ThreadLocalContextManager.instance.getContext().setAttribute(HTTP_REQ, req);
		ThreadLocalContextManager.instance.getContext().setAttribute(HTTP_RES, res);
		
		if (autoSetParamToReqAttr){
			Enumeration nms = req.getParameterNames();
			while (nms.hasMoreElements()) {
				String name = (String) nms.nextElement();
				req.setAttribute(name, req.getParameter(name));
			}
		}
	}

	

	public HttpServletRequest getReq() {
		return (HttpServletRequest) ThreadLocalContextManager.instance.getContext().getAttribute(HTTP_REQ);
	}

	public HttpServletResponse getRes() {
		return (HttpServletResponse) ThreadLocalContextManager.instance.getContext().getAttribute(HTTP_RES);
	}
	
	/**
	 * 获取Request流中json内容
	 * @return
	 */
	public String getStreamJsonContent(){
		//最终修改成这样
//		return ThreadLocalContextManager.getRequestInfo().getContent().getJsonContent();
		//目前暂时这样
		return StreamContentKit.getContent(getReq());
	}

	public String getParam(String nm) {
		return getReq().getParameter(nm);
	}

	public Object getAttr(String nm) {
		return getReq().getAttribute(nm);
	}

	public Set<String> getAttrNames() {
		HashSet<String> ret = new HashSet<String>();
		Enumeration<String> nms = getReq().getAttributeNames();

		while (nms.hasMoreElements()) {
			String key = nms.nextElement();
			ret.add(key);
		}
		return ret;
	}

	public String getStringAttr(String nm) {
		return (String) getReq().getAttribute(nm);
	}

	public void setAttr(String nm, Object o) {
		getReq().setAttribute(nm, o);
	}

	public void forward(String path) {
		try {
			ServletRequest req=getReq();
			req.getRequestDispatcher(path).forward(req, getRes());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public void renderJsp(String jsp) {
		if (jsp.startsWith("/")){
			this.forward(jsp);
		}else{
			String name = this.getClass().getName();
			String jsppath;
			if (name.indexOf('.') > 0) {
				name = name.substring(0, name.lastIndexOf('.'));
				name = StringKit.replaceStr(name, ".", "/");
				jsppath = JSP_BASE + name + "/" + jsp;
			} else {
				jsppath = JSP_BASE + jsp;
			}
			this.forward(jsppath);
		}
	}

	public void renderJson(String json) {
		getRes().setContentType("text/html; charset=utf-8");
		try {
			getRes().getWriter().print(json);
		} catch (IOException e) {
			throw new RuntimeException("render json error" + json, e);
		}
	}
	
	public void renderJson(JsonResult jr){
		ServletRequest req=getReq();
		String wa_ck = (String) req.getAttribute(WA_CK);
		if ("true".equals(wa_ck)) {
			// 为了减少一遍序列化,先假定wa_ch=true,再取MD5,然后再替换之
			jr._setProp(WA_CH, "true");
			jr._setProp(WA_MD5, "%{MD5KEY}%");
			String jsonToWrite = jr.toJson();

			String oldMd5 = (String) req.getAttribute(WA_MD5);
			String newMd5 = MD5Kit.MD5(jsonToWrite);
			if (newMd5.equals(oldMd5)) {
				// 输出：无变化
				JsonResult newrr = JsonResult.create();
				newrr._setProp(WA_CH, "false");
				jsonToWrite = newrr.toJson();
			} else {
				// 输出包含MD5信息的新内容，MD5值替换进去
				jsonToWrite = StringKit.replaceStr(jsonToWrite, "%{MD5KEY}%",
						newMd5);
			}
			renderJson(jsonToWrite);
		} else {
			// 直接输出
			renderJson(jr.toJson());
		}
	}
	
	@Deprecated
	public void renderJson(RuleResult rr) {
		ServletRequest req=getReq();
		String wa_ck = (String) req.getAttribute(WA_CK);
		if ("true".equals(wa_ck)) {
			// 为了减少一遍序列化,先假定wa_ch=true,再取MD5,然后再替换之
			rr.setContent(WA_CH, "true");
			rr.setContent(WA_MD5, "%{MD5KEY}%");
			String jsonToWrite = rr.getJson();

			String oldMd5 = (String) req.getAttribute(WA_MD5);
			String newMd5 = MD5Kit.MD5(jsonToWrite);
			if (newMd5.equals(oldMd5)) {
				// 输出：无变化
				RuleResult newrr = RuleResult.create();
				newrr.setContent(WA_CH, "false");
				jsonToWrite = newrr.getJson();
			} else {
				// 输出包含MD5信息的新内容，MD5值替换进去
				jsonToWrite = StringKit.replaceStr(jsonToWrite, "%{MD5KEY}%",
						newMd5);
			}
			renderJson(jsonToWrite);
		} else {
			// 直接输出
			renderJson(rr.getJson());
		}
	}

	public void invalidSession() {
		HttpServletRequest req=getReq();
		req.getSession().invalidate();
	}

	public void setSessAttr(String k, Object v) {
		HttpServletRequest req = getReq();
		req.getSession().setAttribute(k, v);
	}

	public void sendRedirect(String path) {
		HttpServletResponse res = getRes();
		try {
			res.sendRedirect(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
