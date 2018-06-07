package test.net.jplugin.core.ctx;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.ExtensionCtxHelper;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import test.net.jplugin.core.ctx.bind.BindTest;
import test.net.jplugin.core.ctx.bindrulemethodfilter.TestFroRuleMethodFilter;

public class Plugin extends AbstractPluginForTest {
	
	public Plugin(){
		ExtensionCtxHelper.addRuleExtension(this,IRuleTest.class, RuleTestImpl.class);
		
		ExtensionCtxHelper.addTxMgrListenerExtension(this, TxManagerListenerTest.class);
		
		ExtensionCtxHelper.autoBindRuleServiceExtension(this, "");
		
		ExtensionCtxHelper.autoBindRuleMethodInterceptor(this, "");
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.CTX+1;
	}
	
	@Override
	public void test() throws Throwable {
		AssertKit.assertNotNull(RuleServiceFactory.getRuleService(IRuleTest.class),"rulesvc");
		RuleServiceFactory.getRuleService(IRuleTest.class).testNoMeta();
		RuleServiceFactory.getRuleService(IRuleTest.class).testNoMeta("a");
		RuleServiceFactory.getRuleService(IRuleTest.class).testNoMeta2();
		
		try {
			RuleServiceFactory.getRuleService(IRuleTest.class).testNoMetaWithException();
			throw new RuntimeException("can't come here");
		} catch (Exception e) {
			AssertKit.assertEqual(e.getMessage(), "HAHAHA");
		}
		
		new BindTest().test();
		
		new TestFroRuleMethodFilter().test();
	}

}
