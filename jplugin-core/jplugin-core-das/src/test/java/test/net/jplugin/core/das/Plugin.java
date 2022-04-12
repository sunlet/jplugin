package test.net.jplugin.core.das;

import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import test.net.jplugin.core.das.router.dsnamed.DsNamedDataSourceTest;
import test.net.jplugin.core.das.select_router.SelectRouterTableNotCfgTest;
import test.net.jplugin.core.das.select_router.SelectRouterTest;


public class Plugin extends AbstractPluginForTest {

	public Plugin() {
		ExtensionDasHelper.addSqlListenerExtension(this, SqlMonitorListenerTest.class);
	}
	@Override
	public void test() throws Throwable {
		new SqlTemplateTest().test();
		new DsNamedDataSourceTest().test();
		new SelectRouterTableNotCfgTest().test();
		new SelectRouterTest().test();
	}

	@Override
	public boolean searchClazzForExtension() {
		return false;
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_DDS+1;
	}

}
