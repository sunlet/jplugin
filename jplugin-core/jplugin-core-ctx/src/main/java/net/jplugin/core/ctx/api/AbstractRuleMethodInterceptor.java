package net.jplugin.core.ctx.api;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.ctx.impl.filter4clazz.RuleCallFilterDefine;

/**
 * 通过继承这个类实现对于Rule方法的拦截机制
 * @author Administrator
 *
 */
public abstract class AbstractRuleMethodInterceptor{

//	RuleCallFilterDefine filterDefine;
	
	/**
	 * 这个方法由框架来调用
	 * @param filterDefine
	 */
//	public void setFilterDefine(RuleCallFilterDefine filterDefine) {
//		this.filterDefine = filterDefine;
//	}
	
	public final Object filter(FilterChain fc, RuleServiceFilterContext ctx,RuleCallFilterDefine filterDefine) throws Throwable {
		if (filterDefine.matchMethod(ctx.getMethod().getName())){
			return filterRuleMethod(fc,ctx);
		}else{
			return fc.next(ctx);
		}
	}

	/**
	 * 子类实现时，需要调用fc.next(ctx) 去执行实际的业务。
	 * @param fc
	 * @param ctx
	 * @return
	 * @throws Throwable
	 */
	public abstract Object filterRuleMethod(FilterChain fc, RuleServiceFilterContext ctx) throws Throwable;

}
