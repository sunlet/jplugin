package net.jplugin.common.kits.reso;

import java.io.IOException;

public class Logger {

	private static Logger instance=new Logger();

	public static Logger getLogger(Class class1) {
		return instance;
	}

	public void debug(String string) {
	}

	public void warn(String string) {
	}

	public void error(String string) {
		System.out.println("Error-"+string);
	}

	public void error(String string, Throwable th) {
		th.printStackTrace();
		System.out.println("Error-"+string);
	}

}
