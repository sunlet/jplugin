package test.net.jplugin.ext.gtrace;

import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import test.net.jplugin.ext.gtrace.imp.ESFRPCTest;
import test.net.jplugin.ext.gtrace.imp.ESFRestTest;
import test.net.jplugin.ext.gtrace.imp.ExecutorKitFilterTest;
import test.net.jplugin.ext.gtrace.imp.RestForESFTest;
import test.net.jplugin.ext.gtrace.imp.ServiceCallTest;

public class Plugin extends AbstractPluginForTest {
	public Plugin() {
		//test esf
		ExtensionWebHelper.addServiceExportExtension(this, "/esfRestTest", RestForESFTest.class);
		ExtensionWebHelper.addServiceExportExtension(this,"/testReqId",ServiceCallTest.class);
	}
	@Override
	public void test() throws Throwable {
		new ESFRPCTest().test();
		new ESFRestTest().test();
		new ExecutorKitFilterTest().test();
		ServiceCallTest.calltest();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.GTRACE+1;
	}

}
