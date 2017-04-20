package net.jplugin.core.kernel;

import net.jplugin.core.kernel.api.PluginEnvirement;

public class PluginApp {
	public static void main(String[] args) {
		PluginEnvirement.getInstance().startup();
	}
}
