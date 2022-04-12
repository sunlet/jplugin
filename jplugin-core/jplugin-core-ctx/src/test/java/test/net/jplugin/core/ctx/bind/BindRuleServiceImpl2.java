package test.net.jplugin.core.ctx.bind;

import net.jplugin.core.ctx.api.BindRuleService;


@BindRuleService(interfaceClass=IBindRuleService2.class)
@BindRuleService(interfaceClass=IBindRuleService3.class)
public class BindRuleServiceImpl2 implements IBindRuleService,IBindRuleService2,IBindRuleService3{

	@Override
	public void m2() {
		System.out.println("in m2");
	}

	@Override
	public void m1() {
		System.out.println("in m1");
	}

	@Override
	public void mmmm3() {
		System.out.println("in m3");
		
	}



}
