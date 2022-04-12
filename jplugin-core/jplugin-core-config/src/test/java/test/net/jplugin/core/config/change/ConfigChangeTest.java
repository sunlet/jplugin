package test.net.jplugin.core.config.change;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.config.impl.ConfigureChangeManager;

public class ConfigChangeTest {

	public static void test(){
		List<String> l= new ArrayList<>();
		l.add("group1.key1");
		l.add("group1.key2");
		l.add("group2.key1");
		l.add("group2.key2");
		ConfigureChangeManager.instance.fireConfigChange(l);
		ConfigChangeHandler1.valid();
		ConfigChangeHandler2.valid();
	}
}
