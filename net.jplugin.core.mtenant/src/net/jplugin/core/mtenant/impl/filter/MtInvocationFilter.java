package net.jplugin.core.mtenant.impl.filter;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.ext.webasic.api.IInvocationFilter;
import net.jplugin.ext.webasic.api.InvocationContext;

public class MtInvocationFilter implements IInvocationFilter{
	enum ReqParamAt{BOTH,SESSION,REQUEST}
	private ReqParamAt paraAt;
	private String reqParamName;

	public MtInvocationFilter(){
		String reqParamAt = ConfigFactory.getStringConfig("mtenant.req-param-at");
		reqParamName = ConfigFactory.getStringConfig("mtenant.req-param-name");
		if (StringKit.isNull(reqParamAt)) throw new RuntimeException("config mtenant.req-param-at is  null");
		if (StringKit.isNull(reqParamName)) {
			reqParamAt = "BOTH";
		}
		System.out.println("@@@mtenant.req-param-at="+reqParamAt);
		System.out.println("@@@mtenant.req-param-name="+reqParamName);
		
		paraAt = ReqParamAt.valueOf(reqParamAt);
	}
	/**
	 * <pre>
	 * #HTTP请求中租户参数位置
	 *  mtenant.req-param-at=BOTH|SESSION|COOKIE
	 *  
	 * #HTTP请求中租户参数名称，参数中的名称、cookie中的名称都用这个配置，必须是相同的
	 * mtenant.req-param-name=xxxxx
	 *  
	 * </pre>
	 */
	@Override
	public boolean before(InvocationContext ctx) {
		RequesterInfo reqInfo = ctx.getRequestInfo();
		
		if (paraAt==ReqParamAt.REQUEST){
			String v1 = reqInfo.getContent().getParamContent().get(reqParamName);
			if (!StringKit.isNull(v1)){
				reqInfo.setCurrentTenantId(v1);
			}
		}
		if (paraAt==ReqParamAt.SESSION){
			String v1 = reqInfo.getCookies().getCookie(reqParamName);
			if (!StringKit.isNull(v1)){
				reqInfo.setCurrentTenantId(v1);
			}
		}
		if (paraAt == ReqParamAt.BOTH){
			String v1 = reqInfo.getContent().getParamContent().get(reqParamName);
			if (!StringKit.isNull(v1)){
				reqInfo.setCurrentTenantId(v1);
			}else{
				v1 = reqInfo.getCookies().getCookie(reqParamName);
				if (!StringKit.isNull(v1)){
					reqInfo.setCurrentTenantId(v1);
				}
			}
		}
		return true;
	}

	@Override
	public void after(InvocationContext ctx) {
	}

}
