package test.net.jplugin.core.das.mybatis.anno;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.mybatis.api.RefMybatisService;
import net.jplugin.core.das.mybatis.impl.IMybatisService;
import net.jplugin.core.das.mybatis.impl.MybaticsServiceImplNew;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;

public class RuleTestForMybatisAnno extends RefAnnotationSupport implements IRuleTestForMybatisAnno{
	@RefMybatisService
	IMybatisService m1;
	
	@RefMybatisService(dataSource = "database")
	IMybatisService m2;
	
	IMybatisService m3;
	
	@RefLogger
	private Logger logger;
	@RefLogger
	private static Logger logger2=LogFactory.getLogger(String.class);
	@Override
	public void test() {
		AssertKit.assertTrue(m1!=null);
		AssertKit.assertTrue(m2!=null);
		AssertKit.assertTrue(m3==null);
		AssertKit.assertTrue(logger!=null);
		AssertKit.assertTrue(logger2!=null);
	}

	
}
