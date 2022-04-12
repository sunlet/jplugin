package test.net.jplugin.core.ctx.bindrulemethodfilter;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.service.api.RefService;

public class TestFroRuleMethodFilter extends RefAnnotationSupport{

	@RefService
	IRuleService123 svc;
	public void test(){
		svc.setA("hahaha");
		AssertKit.assertEqual("hahaha",svc.getA());
		
		AssertKit.assertEqual(4, RuleMethodFilter1.cnt);
		AssertKit.assertEqual(1, RuleMethodFilter2.cnt);
		
		svc.test();
		AssertKit.assertEqual(2, RuleMethodFilter3.cnt);
				
	}
}
