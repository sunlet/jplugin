package test.net.jplugin.core.ctx.bind;

import net.jplugin.core.ctx.api.RefRuleService;
import net.jplugin.core.kernel.api.RefAnnotationSupport;

public class BindTest extends RefAnnotationSupport{
	
	@RefRuleService
	IBindRuleService svc;
	@RefRuleService 
	IBindRuleService2 svc2;
	@RefRuleService
	IBindRuleService3 svc3;
	public  void test(){
		svc.m1();
		svc2.m2();
		svc3.mmmm3();
	}
}
