package test.net.jplugin.core.mtenant;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.mtenant.impl.kit.SqlMultiTenantHanlderKit;

public class Plugin extends AbstractPluginForTest {
	@Override
	public void test() throws Throwable {
//		String result = SqlMultiTenantHanlderKit.handle("mt-ds", "select * from tb1 wehre f1=1");
//		AssertKit.assertEqual(result, "select * from tb1 where _mtenant_='1001' and (f1=1)");
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.MULTI_TANANT+1;
	}
}
