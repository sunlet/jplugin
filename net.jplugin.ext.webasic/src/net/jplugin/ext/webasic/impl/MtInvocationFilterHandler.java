package net.jplugin.ext.webasic.impl;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
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
	private boolean nullTenantAllowed; //default value is true
	private boolean enable;

	public MtInvocationFilterHandler(){
		enable = "true".equalsIgnoreCase(ConfigFactory.getStringConfigWithTrim("mtenant.enable"));
		if (!enable)
			return;
		
		String reqParamAt = ConfigFactory.getStringConfigWithTrim("mtenant.req-param-at");
		reqParamName = ConfigFactory.getStringConfigWithTrim("mtenant.req-param-name");
		reqDefaultTenant = ConfigFactory.getStringConfigWithTrim("mtenant.req-default-tenant");
		nullTenantAllowed = !"false".equalsIgnoreCase(ConfigFactory.getStringConfigWithTrim("mtenant.null-tenant-allowed"));
		
		if (StringKit.isNull(reqParamAt)) throw new RuntimeException("config mtenant.req-param-at is  null");
		if (StringKit.isNull(reqParamName)) {
			reqParamAt = "BOTH";
		}
		PluginEnvirement.INSTANCE.getStartLogger().log("@@@mtenant.req-param-at="+reqParamAt);
		PluginEnvirement.INSTANCE.getStartLogger().log("@@@mtenant.req-param-name="+reqParamName);
		PluginEnvirement.INSTANCE.getStartLogger().log("@@@mtenant.req-default-tenant="+reqDefaultTenant);
		PluginEnvirement.INSTANCE.getStartLogger().log("@@@mtenant.null-tenant-allowed="+nullTenantAllowed);
		
		paraAt = ReqParamAt.valueOf(reqParamAt);
	}
	/**
	 */
//	@Override
	public void handle(RequesterInfo reqInfo) {
		if (!enable) 
			return;
		
		String v1;
		if (paraAt==ReqParamAt.REQUEST){
			v1 = reqInfo.getContent().getParamContent().get(reqParamName);
		}else if (paraAt==ReqParamAt.COOKIE){
			v1 = reqInfo.getCookies().getCookie(reqParamName);
		}else if (paraAt == ReqParamAt.BOTH){
			v1 = reqInfo.getContent().getParamContent().get(reqParamName);
			if (StringKit.isNull(v1))
				v1 = reqInfo.getCookies().getCookie(reqParamName);
		}else{
			throw new RuntimeException("Error ReqParamAt value:"+paraAt);
		}
		
		//set
		checkAndSet(reqInfo,v1);
	}
	
	/**
	 * 检查是否允许为null，并处理默认值.HTTP RPC都调用
	 * @param v
	 * @return
	 */
	public void checkAndSet(RequesterInfo reqInfo,String v){
		if (!enable)  //因为可能是RPC调用，再检查一次
			return;
		
		//not null return it
		if (v!=null && !"".equals(v)){
			reqInfo.setCurrentTenantId(v);
			return;
		}

		if (StringKit.isNotNull(reqDefaultTenant)){
			//设置一个标志位，目前用了默认的tenant
			ThreadLocalContextManager.getCurrentContext().setAttribute(ThreadLocalContext.ATTR_USING_DEF_TENANT, true);
			reqInfo.setCurrentTenantId(reqDefaultTenant);
		}else{
			// check allow null
			if (!nullTenantAllowed){
				throw new RuntimeException("Mtenant is enabled , config [null-tenant-allowed] is false, but the tenantid is null. url:" + reqInfo.getRequestUrl());
			}
		}
	}	
	
	public static void init() {
		instance = new MtInvocationFilterHandler();
	}

//	@Override
//	public void after(InvocationContext ctx) {
//	}

}
