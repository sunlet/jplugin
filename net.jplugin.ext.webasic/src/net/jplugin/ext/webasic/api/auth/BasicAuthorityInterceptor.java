package net.jplugin.ext.webasic.api.auth;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.ctx.api.AbstractRuleMethodInterceptor;
import net.jplugin.core.ctx.api.RuleServiceFilterContext;
import net.jplugin.core.kernel.api.Initializable;

/**
 * <pre>
 * 这个类是用以权限校验的基类，可以继承该类实现 SSO，User，Tenant,APP等验证拦截器抽象类。
 * 
 * 基类控制 permissionMap 的内容，并实现validate方法，即可实现权限控制
 * 
 * </pre>
 * @author LiuHang
 *
 */
public abstract class BasicAuthorityInterceptor extends AbstractRuleMethodInterceptor implements Initializable{
	private Map<Class,Map<String,String[]>> permissionMap = new HashMap<Class,Map<String, String[]>>();
	/**
	 * 如果抛出异常，肯定验证失败了，并且方法抛出异常
	 * 如果没有抛出异常，返回false了，则可能在验证过程中处理了（比如ErrorPage），则正常返回
	 * @param ctx
	 * @param permissions
	 * @return
	 */
	public abstract boolean validate(RuleServiceFilterContext ctx,String[] permissions);
	
	protected void setPermissionMap(Map<Class, Map<String, String[]>> map){
		this.permissionMap = map;
	}
	
	@Override
	public Object filterRuleMethod(FilterChain fc, RuleServiceFilterContext ctx) throws Throwable {
		
		//获取方法名
		String mn = ctx.getMethod().getName();
		
		//获取授权列表
		String[] perms;
		Map<String, String[]> clazzMap = this.permissionMap.get(ctx.getObject().getClass());
		if (clazzMap==null){
			perms = null;
		}else{
			perms = clazzMap.get(mn);
			if (perms!=null && perms.length==0){
				perms = null;
			}
		}
		//验证返回
		if (validate(ctx,perms))
			return fc.next(ctx);
		else
			return null;
	}
}
