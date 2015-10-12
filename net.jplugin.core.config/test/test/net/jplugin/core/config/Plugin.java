package test.net.jplugin.core.config;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;

public class Plugin extends AbstractPluginForTest{

	@Override
	public void test() throws Throwable {
		AssertKit.assertEqual("1",ConfigFactory.getStringConfig("test1.c1"));
		AssertKit.assertEqual("2",ConfigFactory.getStringConfig("test1.c2"));
		AssertKit.assertEqual("1",ConfigFactory.getStringConfig("test2.c1"));
		AssertKit.assertEqual("2",ConfigFactory.getStringConfig("test2.c2"));
		AssertKit.assertEqual(2,ConfigFactory.getIntConfig("test2.c2"));
		AssertKit.assertEqual(2l,ConfigFactory.getLongConfig("test2.c2"));
		AssertKit.assertEqual(null,ConfigFactory.getLongConfig("testaaaaa.c2"));
		AssertKit.assertEqual(100l,ConfigFactory.getLongConfig("testaaaaa.c2",100l));
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.CONFIG+1;
	}

}
