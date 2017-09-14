package test.net.jplugin.core.ctx.bind;

import net.jplugin.core.ctx.api.BindRuleService;


@BindRuleService(interfaceClass=IBindRuleService2.class)
public class BindRuleServiceImpl2 implements IBindRuleService,IBindRuleService2{

	@Override
	public void m2() {
		System.out.println("in m2");
	}

	@Override
	public void m1() {
		System.out.println("in m1");
	}



}
