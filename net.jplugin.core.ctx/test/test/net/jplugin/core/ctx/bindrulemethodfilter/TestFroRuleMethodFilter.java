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
	}
}
