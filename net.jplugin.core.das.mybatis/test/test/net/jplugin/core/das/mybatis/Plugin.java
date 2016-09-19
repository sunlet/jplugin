package test.net.jplugin.core.das.mybatis;

import net.jplugin.core.ctx.ExtensionCtxHelper;
import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.das.mybatis.api.ExtensionMybatisDasHelper;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import test.net.jplugin.core.das.mybatis.ts.DbCreate;
import test.net.jplugin.core.das.mybatis.ts.RouteTest;
import test.net.jplugin.core.das.mybatis.ts.TbRoute0Mapper;
import test.net.luis.plugin.das.mybatis.annotest.AnnoBaticsTest;
import test.net.luis.plugin.das.mybatis.annotest.IMybtestMapper;
import test.net.luis.plugin.das.mybatis.annotest.IRule;
import test.net.luis.plugin.das.mybatis.annotest.RuleService;
import test.net.luis.plugin.das.mybatis.sesstest.SessionTest;
import test.net.luis.plugin.das.mybatis.txtest.ITxTestDB1Mapper;
import test.net.luis.plugin.das.mybatis.txtest.ITxTestDB2Mapper;
import test.net.luis.plugin.das.mybatis.txtest.TxTest;
import test.net.luis.plugin.das.mybatis.xmltest.IXMLMapper;
import test.net.luis.plugin.das.mybatis.xmltest.XMLBaticsTest;
import test.net.luis.plugin.das.mybatis.xmltest.XMLBaticsTest2DB_2;

public class Plugin extends AbstractPluginForTest{

	public Plugin(){
		ExtensionMybatisDasHelper.addMappingExtension(this, IMybtestMapper.class);
		ExtensionMybatisDasHelper.addMappingExtension(this, IXMLMapper.class);
		ExtensionCtxHelper.addRuleExtension(this, IRule.class.getName(),IRule.class, RuleService.class);
		
		ExtensionDasHelper.addDataSourceExtension(this,"testdb", "db_2");
		ExtensionMybatisDasHelper.addMappingExtension(this, "testdb",IXMLMapper.class);

		ExtensionMybatisDasHelper.addMappingExtension(this, "testdb",ITxTestDB1Mapper.class);
		ExtensionMybatisDasHelper.addMappingExtension(this, "database",ITxTestDB2Mapper.class);
	
		//这里重用了das模块测试用例的数据源配置
		ExtensionMybatisDasHelper.addMappingExtension(this,"router-db", TbRoute0Mapper.class);
	}
	@Override
	public void test() throws Throwable {
		new AnnoBaticsTest().test();
		new XMLBaticsTest().test();
		new XMLBaticsTest2DB_2().test();
		
		new TxTest().test();
		
		new SessionTest().test();
		
		DbCreate.create();
		new RouteTest().test();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_IBATIS+1;
	}

}
