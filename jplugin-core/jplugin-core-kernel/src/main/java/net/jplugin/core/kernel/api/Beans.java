package net.jplugin.core.kernel.api;

import javafx.beans.DefaultProperty;
import net.jplugin.common.kits.StringKit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 已经不推荐使用，请使用ExtensionObjects类的对应方法。
 */
@Deprecated
public class Beans {

	public static <T> T get(String id,Class<T> t) {
		return (T) get(id);
	}
	
	public static Object get(String id) {
		return ExtensionObjects.get(id);
	}
	public static Object find(String id) {
		return ExtensionObjects.find(id);
	}

}
