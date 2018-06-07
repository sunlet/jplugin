package test.net.jplugin.core.ctx.bindrulemethodfilter;

import net.jplugin.core.ctx.api.BindRuleService;
import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.service.api.BindService;

@BindRuleService
public class RuleService123 implements IRuleService123{

	private String a;

	@Rule
	@Override
	public String getA() {
		return this.a;
		
	}

	@Rule
	@Override
	public void setA(String a) {
		this.a = a;
		
	}

}
