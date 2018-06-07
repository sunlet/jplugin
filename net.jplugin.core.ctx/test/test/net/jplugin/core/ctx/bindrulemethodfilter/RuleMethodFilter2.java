package test.net.jplugin.core.ctx.bindrulemethodfilter;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.ctx.api.AbstractRuleMethodInterceptor;
import net.jplugin.core.ctx.api.BindRuleMethodInterceptor;
import net.jplugin.core.ctx.api.RuleServiceFilterContext;

@BindRuleMethodInterceptor(applyTo="test.net.jplugin.core.ctx.bindrulemethodfilter.RuleService123:set*",sequence=2)
public class RuleMethodFilter2 extends AbstractRuleMethodInterceptor {

	@Override
	public Object filterRuleMethod(FilterChain fc, RuleServiceFilterContext ctx) throws Throwable {
		System.out.println("!!!!!!!!!!!!RuleMethodFilter2" +ctx.getMethod().getName());
		return fc.next(ctx);
	}

}
