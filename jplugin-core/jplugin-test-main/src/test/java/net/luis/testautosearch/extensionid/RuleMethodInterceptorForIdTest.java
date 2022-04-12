package net.luis.testautosearch.extensionid;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.ctx.api.AbstractRuleMethodInterceptor;
import net.jplugin.core.ctx.api.BindRuleMethodInterceptor;
import net.jplugin.core.ctx.api.RuleServiceFilterContext;

@BindRuleMethodInterceptor(applyTo = "*" ,id="RuleMethodInterceptorForIdTest1")
@BindRuleMethodInterceptor(applyTo = "*" ,id="RuleMethodInterceptorForIdTest2")
public class RuleMethodInterceptorForIdTest  extends AbstractRuleMethodInterceptor{

	public RuleMethodInterceptorForIdTest() {
		System.out.println("initing........");
	}
	
	public void aaa() {
		
	}
	
	@Override
	public Object filterRuleMethod(FilterChain fc, RuleServiceFilterContext ctx) throws Throwable {
		return fc.next(ctx);
	}

}
