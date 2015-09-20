package net.jplugin.ext.webasic.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.token.api.ITokenService;
import net.jplugin.ext.webasic.api.WebFilter;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-5 上午10:25:57
 **/

public class InitRequestInfoFilter implements WebFilter {
	private static final String TOKEN = "_token";
	private static final String CLIENTTYPE = "_ctype";
	private static final String OPERATORID = "_operatorId";
	private static final String CLIENTVERSION = "_version";

	public static boolean dummyAllowed = false;
	private static String dummyToken=null;
	/* (non-Javadoc)
	 * 如果传入了token，则不用session
	 * @see net.luis.plugin.webservice.api.WebFilter#doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public boolean doFilter(HttpServletRequest req, HttpServletResponse res) {
		res.addHeader("Access-Control-Allow-Origin","*");
		
		ThreadLocalContextManager.getRequestInfo().setCurrentTenantId(req.getParameter("_gid"));
		String _tk = req.getParameter("_tk");
		if (_tk!=null){
			ITokenService tksvc = RuleServiceFactory.getRuleService(ITokenService.class);
			Map<String, String> tkinfo = tksvc.validAndGetTokenInfo(_tk);
			RequesterInfo info = ThreadLocalContextManager.instance.getContext().getRequesterInfo();
			info.setToken(_tk);
			info.setClientType(tkinfo.get("_client"));
			info.setOperatorId(tkinfo.get("_user"));
			info.setClientVersion(tkinfo.get("_ver"));
			return true;
		}else{
			if (dummyAllowed) {
				if (dummyToken==null){
					synchronized (this) {
						if (dummyToken==null){
							ITokenService tksvc = RuleServiceFactory.getRuleService(ITokenService.class);
							Map<String, String> dummyTkInfo = new HashMap<String, String>();
							dummyTkInfo.put("_user", "dummy");
							dummyToken = tksvc.createToken(dummyTkInfo, "dummy", "default");
						}
					}
				}
				RequesterInfo info = ThreadLocalContextManager.instance.getContext().getRequesterInfo();
				info.setToken(dummyToken);
				info.setOperatorId("dummy");
				return true;
			}else
				return true;
		}
			
//		RequesterInfo info = ThreadLocalContextManager.instance.getContext().getRequesterInfo();
//		info.setToken(req.getParameter(TOKEN));
//		info.setClientType(req.getParameter(CLIENTTYPE));
//		info.setOperatorId((String) req.getSession().getAttribute("_user"));
//		info.setClientVersion(req.getParameter(CLIENTVERSION));
//		return true;
	}

}
