package test.net.jplugin.core.das.mybatis;

import test.net.luis.plugin.das.mybatis.annotest.AnnoBaticsTest;
import test.net.luis.plugin.das.mybatis.annotest.IMybtestMapper;
import test.net.luis.plugin.das.mybatis.annotest.IRule;
import test.net.luis.plugin.das.mybatis.annotest.RuleService;
import test.net.luis.plugin.das.mybatis.xmltest.IXMLMapper;
import test.net.luis.plugin.das.mybatis.xmltest.XMLBaticsTest;
import net.jplugin.core.ctx.ExtensionCtxHelper;
import net.jplugin.core.das.mybatis.api.ExtensionMybatisDasHelper;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;

public class Plugin extends AbstractPluginForTest{

	public Plugin(){
		ExtensionMybatisDasHelper.addMappingExtension(this, IMybtestMapper.class);
		ExtensionMybatisDasHelper.addMappingExtension(this, IXMLMapper.class);
		ExtensionCtxHelper.addRuleExtension(this, IRule.class.getName(),IRule.class, RuleService.class);
	}
	@Override
	public void test() throws Throwable {
		new AnnoBaticsTest().test();
		new XMLBaticsTest().test();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_IBATIS+1;
	}

}
