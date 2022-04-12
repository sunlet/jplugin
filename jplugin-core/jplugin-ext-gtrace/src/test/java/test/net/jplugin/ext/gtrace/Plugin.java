package test.net.jplugin.ext.gtrace;

import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionKernelHelper;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import test.net.jplugin.ext.gtrace.imp.ESFRPCTest;
import test.net.jplugin.ext.gtrace.imp.ESFRestTest;
import test.net.jplugin.ext.gtrace.imp.ExecutorKitFilterTest;
import test.net.jplugin.ext.gtrace.imp.RestForESFTest;
import test.net.jplugin.ext.gtrace.imp.ServiceCallTest;
import test.net.jplugin.ext.gtrace.schedule.ScheduTest;
import test.net.jplugin.ext.gtrace.schedule.ScheduleFilter;

public class Plugin extends AbstractPluginForTest {
	public Plugin() {
		//test esf
		ExtensionWebHelper.addServiceExportExtension(this, "/esfRestTest", RestForESFTest.class);
		ExtensionWebHelper.addServiceExportExtension(this,"/testReqId",ServiceCallTest.class);
		
		ExtensionKernelHelper.addScheduledExecutionFilterExtension(this,ScheduleFilter.class);
	}
	@Override
	public void test() throws Throwable {
		new ESFRPCTest().test();
		new ESFRestTest().test();
		new ExecutorKitFilterTest().test();
		ServiceCallTest.calltest();

//		ScheduTest.init();
//		ScheduTest.instance.test();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.GTRACE+1;
	}
	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}

}
