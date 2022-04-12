package net.luis.main;

import net.jplugin.core.kernel.PluginApp;
import net.jplugin.core.kernel.api.IStartLogger;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class TestStartLogger {
	public static void main(String[] args) throws InterruptedException {
		new PluginApp().main(null);
		IStartLogger logger = PluginEnvirement.getInstance().getStartLogger();
		
		for (int i=0;i<100000;i++){
			logger.write(i);
			logger.write(" ");
			logger.write(i+1);
			logger.log("abcabc");
			logger.log("xyz",new Exception());
		}
		System.out.println("write ok!");
	}
}
