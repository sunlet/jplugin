package test.net.jplugin.ext.webasic.annotation;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.RefRuleService;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;

public class WebControllerTest {
	@RefLogger
	private Logger logger;
	@RefLogger
	private static Logger logger2=LogFactory.getLogger(String.class);
	
	@RefRuleService
	IRuleTestForAnno ruletest;
	
	@RefRuleService(name="rule1346")
	IRuleTestForAnno ruletest1;
	
	public void test(HttpServletRequest req,HttpServletResponse res) throws IOException {
		AssertKit.assertNotNull(ruletest, "rule ref");
		ruletest.test();
		AssertKit.assertNotNull(ruletest1, "rule1 ref");
		ruletest1.test();
		
		AssertKit.assertTrue(logger!=null);
		AssertKit.assertTrue(logger2!=null);
		res.getWriter().write("1");
	}
}
