package test.net.jplugin.core.mtenant;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.mtenant.ExtensionMTenantHelper;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import test.net.jplugin.core.mtenant.iterator.TenantIteratorTest;
import test.net.jplugin.core.mtenant.iterator.TenantListProvider;
import test.net.jplugin.core.mtenant.mdbsqlhelper.MultidbSqlHelperTest;
import test.net.jplugin.core.mtenant.sqlhandlervisit.SqlHandlerVisitorTest;

public class Plugin extends AbstractPluginForTest {
	public Plugin() {
		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable"))) {
			ExtensionWebHelper.addServiceExportExtension(this, "/mtenant", TestCtrller.class);
			
			ExtensionMTenantHelper.addTenantListProvidorExtension(this, TenantListProvider.class);
		}
	}

	@Override
	public void test() throws Throwable {
//		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable"))) {
//			ThreadLocalContextManager.getRequestInfo().setCurrentTenantId("1001");
//			String result = MulTenantForMergeTableHandler.handleSql("mt-ds", "select * from tb1 where f1=1",null);
//			AssertKit.assertEqual(result, "select * from tb1 where _tenant_='1001' and f1=1",null);
//			result = MulTenantForMergeTableHandler.handleSql("mt-ds", "select * from tb1",null);
//			AssertKit.assertEqual(result, "select * from tb1 where _tenant_='1001'");
//			result = MulTenantForMergeTableHandler.handleSql("mt-ds", "select * from (select * from tb1)",null);
//			AssertKit.assertEqual(result, "select * from(select * from tb1 where _tenant_='1001')");
//			
//			ThreadLocalContextManager.getRequestInfo().setCurrentTenantId("test111");
//			// test post
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			String ret = HttpKit.post("http://localhost:8080/demo/mtenant/test.do", map);
//			Map retMap = JsonKit.json2Map(ret);
//			String retVal = (String) ObjectKit.findObject(retMap, "content/result");
//			AssertKit.assertEqual(retVal, "OK");
//		}
		new MultidbSqlHelperTest().test();
		new TenantIteratorTest().testRunnable();
		new TenantIteratorTest().testCallable();
		
		SqlHandlerVisitorTest.test();

	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.MULTI_TANANT + 1;
	}

	@Override
	public boolean searchClazzForExtension() {
		return false;
	}
}
