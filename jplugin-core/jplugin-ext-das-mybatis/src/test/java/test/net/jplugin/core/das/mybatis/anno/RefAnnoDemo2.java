package test.net.jplugin.core.das.mybatis.anno;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.config.api.RefConfig;
import net.jplugin.core.ctx.api.RefRuleService;
import net.jplugin.core.das.mybatis.api.RefMybatisService;
import net.jplugin.core.das.mybatis.impl.IMybatisService;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;

public class RefAnnoDemo2{
	public RefAnnoDemo2() {
		PluginEnvirement.INSTANCE.resolveRefAnnotation(this);
	}
	
	@RefMybatisService
	IMybatisService m1;
	
	@RefMybatisService(dataSource = "database")
	IMybatisService m2;
	
	@RefLogger
	private Logger logger;
	
	@RefConfig(path="test1.c1")
	String cfg1;
	
	@RefConfig(path="test1.c2")
	Integer cfg2;
	
	@RefRuleService
	IRuleTestForMybatisAnno rule1;
	
	
	/**
	 * 静态变量只赋值一次
	 */
	@RefLogger
	private static Logger logger2;
	
	public void test() {
		AssertKit.assertTrue(m1!=null);
		AssertKit.assertTrue(m2!=null);
		AssertKit.assertTrue(logger!=null);
		AssertKit.assertTrue(logger2!=null);
		AssertKit.assertEqual("1", cfg1);
		AssertKit.assertEqual(2, cfg2);
		AssertKit.assertNotNull(rule1, "rule1");
	}
	

}
