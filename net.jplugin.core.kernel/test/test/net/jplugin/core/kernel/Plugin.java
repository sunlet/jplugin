package test.net.jplugin.core.kernel;

import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;

public class Plugin extends AbstractPluginForTest{

	@Override
	public void test() throws Throwable {
		new ExecutorKitFilterTest().test();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.KERNEL+1;
	}

}
