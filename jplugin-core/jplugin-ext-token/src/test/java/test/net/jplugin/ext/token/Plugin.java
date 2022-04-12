package test.net.jplugin.ext.token;

import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.PluginAnnotation;

public class Plugin extends AbstractPluginForTest {

	@Override
	public void test() throws Throwable {
		new TokenTest().test();

	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.TOKEN+1;
	}

	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}

}
