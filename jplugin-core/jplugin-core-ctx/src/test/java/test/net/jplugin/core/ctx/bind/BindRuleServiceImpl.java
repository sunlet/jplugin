package test.net.jplugin.core.ctx.bind;

import net.jplugin.core.ctx.api.BindRuleService;

@BindRuleService
public class BindRuleServiceImpl implements IBindRuleService{

	@Override
	public void m1() {
		System.out.println("in m1");
		
	}

}
