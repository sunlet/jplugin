package net.jplugin.ext.webasic.impl;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.ext.webasic.api.IInvocationFilter;
import net.jplugin.ext.webasic.api.InvocationContext;
/**
 * @author LiuHang
 *
 */
public class MtInvocationFilterHandler{
	public static MtInvocationFilterHandler instance ;
	
	enum ReqParamAt{BOTH,COOKIE,REQUEST}
	private ReqParamAt paraAt;
	private String reqParamName;
	private String reqDefaultTenant;

	public MtInvocationFilterHandler(){
		String reqParamAt = ConfigFactory.getStringConfigWithTrim("mtenant.req-param-at");
		reqParamName = ConfigFactory.getStringConfigWithTrim("mtenant.req-param-name");
		reqDefaultTenant = ConfigFactory.getStringConfigWithTrim("mtenant.req-default-tenant");
		if (StringKit.isNull(reqParamAt)) throw new RuntimeException("config mtenant.req-param-at is  null");
		if (StringKit.isNull(reqParamName)) {
			reqParamAt = "BOTH";
		}
		System.out.println("@@@mtenant.req-param-at="+reqParamAt);
		System.out.println("@@@mtenant.req-param-name="+reqParamName);
		System.out.println("@@@mtenant.req-default-tenant="+reqDefaultTenant);
		
		paraAt = ReqParamAt.valueOf(reqParamAt);
	}
	/**
	 */
//	@Override
	public void handle(RequesterInfo reqInfo) {
//		RequesterInfo reqInfo = ctx.getRequestInfo();
		
		if (paraAt==ReqParamAt.REQUEST){
			String v1 = reqInfo.getContent().getParamContent().get(reqParamName);
			if (!StringKit.isNull(v1)){
				reqInfo.setCurrentTenantId(v1);
			}
		}
		if (paraAt==ReqParamAt.COOKIE){
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
		//set default
		if (StringKit.isNull(reqInfo.getCurrentTenantId())){
			reqInfo.setCurrentTenantId(reqDefaultTenant);
		}
//		return true;
	}
	public static void init() {
		instance = new MtInvocationFilterHandler();
		
	}

//	@Override
//	public void after(InvocationContext ctx) {
//	}

}
