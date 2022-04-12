package test.net.jplugin.core.das.mybatis;

import java.sql.SQLException;

import net.jplugin.core.ctx.ExtensionCtxHelper;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.das.mybatis.api.ExtensionMybatisDasHelper;
import net.jplugin.core.das.mybatis.api.MysqlPageInterceptor;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.service.ExtensionServiceHelper;
import net.jplugin.core.service.api.ServiceFactory;
import test.net.jplugin.core.das.mybatis.anno.IRuleTestForMybatisAnno;
import test.net.jplugin.core.das.mybatis.anno.IServiceForAnno;
import test.net.jplugin.core.das.mybatis.anno.RefAnnoDemo;
import test.net.jplugin.core.das.mybatis.anno.RefAnnoDemo2;
import test.net.jplugin.core.das.mybatis.anno.RuleTestForMybatisAnno;
import test.net.jplugin.core.das.mybatis.anno.ServiceImplForAnno;
import test.net.jplugin.core.das.mybatis.bind.TestBind;
import test.net.jplugin.core.das.mybatis.cachetest.CacheTest;
import test.net.jplugin.core.das.mybatis.cachetest.ICacheMapper;
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
import test.net.luis.plugin.das.mybatis.txtest.MapperProxyTest;
import test.net.luis.plugin.das.mybatis.txtest.RefMapperTest;
import test.net.luis.plugin.das.mybatis.txtest.TxTest;
import test.net.luis.plugin.das.mybatis.xmltest.IXMLMapper;
import test.net.luis.plugin.das.mybatis.xmltest.XMLBaticsTest;
import test.net.luis.plugin.das.mybatis.xmltest.XMLBaticsTest2DB_2;

public class Plugin extends AbstractPluginForTest{

	public Plugin() {
		ExtensionMybatisDasHelper.addMappingExtension(this, IMybtestMapper.class);
		ExtensionMybatisDasHelper.addMappingExtension(this, IXMLMapper.class);
		ExtensionCtxHelper.addRuleExtension(this, IRule.class.getName(),IRule.class, RuleService.class);
		
		ExtensionDasHelper.addDataSourceExtension(this,"testdb", "db_2");
		ExtensionMybatisDasHelper.addMappingExtension(this, "testdb",IXMLMapper.class);

		ExtensionMybatisDasHelper.addMappingExtension(this, "testdb",ITxTestDB1Mapper.class);
		ExtensionMybatisDasHelper.addMappingExtension(this, "database",ITxTestDB2Mapper.class);
	
		//这里重用了das模块测试用例的数据源配置
		ExtensionMybatisDasHelper.addMappingExtension(this,"router-db", TbRoute0Mapper.class);
		
		//测试anno
		ExtensionCtxHelper.addRuleExtension(this, IRuleTestForMybatisAnno.class, RuleTestForMybatisAnno.class);
		ExtensionServiceHelper.addServiceExtension(this, IServiceForAnno.class.getName(), ServiceImplForAnno.class);
		
		ExtensionMybatisDasHelper.autoBindMapperExtension(this, ".bind");
		//测试mybatis缓存
		ExtensionMybatisDasHelper.addMappingExtension(this, ICacheMapper.class);
		ExtensionMybatisDasHelper.addInctprorExtension(this, MysqlPageInterceptor.class);
	}
	@Override
	public void test() throws Throwable {
		RuleServiceFactory.getRuleService(IRuleTestForMybatisAnno.class).test();
		ServiceFactory.getService(IServiceForAnno.class).test();
		
		new TestBind().test();
		new RefAnnoDemo().test();
		new RefAnnoDemo2().test();
		
		new AnnoBaticsTest().test();
		new XMLBaticsTest().test();
		new XMLBaticsTest2DB_2().test();
		
		new TxTest().test();
		new MapperProxyTest().test();
		new MapperProxyTest().test();
		new RefMapperTest().test();
		new RefMapperTest().test();
		
		new SessionTest().test();
		
		DbCreate.create();
		new RouteTest().test();
		
		
		CacheTest.INSTANCE.test();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_IBATIS+1;
	}
	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}

}
