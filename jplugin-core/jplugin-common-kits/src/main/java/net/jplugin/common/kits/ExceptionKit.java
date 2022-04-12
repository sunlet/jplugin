package net.jplugin.common.kits;

import java.lang.reflect.InvocationTargetException;

public class ExceptionKit {

	public static Throwable getRootCause(Throwable t) {
		if (t==null) {
			return null;
		}
		if (t instanceof InvocationTargetException) {
			t = ((InvocationTargetException) t).getTargetException();
		}
		if (t instanceof ExceptionInInitializerError) {
			t = t.getCause();
		}
		return t;
	}

}
