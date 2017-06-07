package net.jplugin.core.kernel.api;

import junit.framework.TestCase;
import net.jplugin.core.kernel.PluginApp;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class BaseTestCase extends TestCase {
	private ThreadLocalContext ctx;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		PluginApp.main(null);
		ctx = ThreadLocalContextManager.instance.createContext();
	}



	@Override
	protected void tearDown() throws Exception {
		ThreadLocalContextManager.instance.releaseContext();
		super.tearDown();
	}
}
