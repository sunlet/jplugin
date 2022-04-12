package net.luis.testautosearch;

import java.util.List;

import net.jplugin.core.kernel.api.BindStartup;
import net.jplugin.core.kernel.api.IStartup;
import net.jplugin.core.kernel.api.PluginError;

@BindStartup


public class StartupAnnoTest implements IStartup {

	public static boolean called = false;
	@Override
	public void startFailed(Throwable th, List<PluginError> errors) {
		System.out.println(" In StartupAnnoTest");
		called = true;
	}

	@Override
	public void startSuccess() {
		System.out.println(" In StartupAnnoTest");
		called = true;
	}

	public static void assertCalled() {
		if (!called) {
			throw new RuntimeException("should called");
		}
	}
}
