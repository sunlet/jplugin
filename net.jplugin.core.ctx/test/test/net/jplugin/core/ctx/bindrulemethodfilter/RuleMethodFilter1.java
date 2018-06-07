package test.net.jplugin.core.ctx.bindrulemethodfilter;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.ctx.api.AbstractRuleMethodInterceptor;
import net.jplugin.core.ctx.api.BindRuleMethodInterceptor;
import net.jplugin.core.ctx.api.RuleServiceFilterContext;

@BindRuleMethodInterceptor(applyTo="test.net.jplugin.core.ctx.bindrulemethodfilter.RuleService123",sequence=1)
@BindRuleMethodInterceptor(applyTo="test.net.jplugin.core.ctx.bindrulemethodfilter.RuleService123,*",sequence=1)
public class RuleMethodFilter1 extends AbstractRuleMethodInterceptor {

	@Override
	public Object filterRuleMethod(FilterChain fc, RuleServiceFilterContext ctx) throws Throwable {
		System.out.println("!!!!!!!!!!!!RuleMethodFilter1." +ctx.getMethod().getName());
		return fc.next(ctx);
	}

}
