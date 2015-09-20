package test.net.jplugin.ext.token;

import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;

public class Plugin extends AbstractPluginForTest {

	@Override
	public void test() throws Throwable {
		new TokenTest().test();

	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.TOKEN+1;
	}

}
