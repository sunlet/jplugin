package test.net.jplugin.core.config.change;

import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.config.api.IConfigChangeContext;
import net.jplugin.core.config.api.IConfigChangeHandler;

public class ConfigChangeHandler2 implements IConfigChangeHandler {

	public static  List<String> changedKeys;

	@Override
	public void onChange(IConfigChangeContext ctx) {
		 changedKeys = ctx.getChangedKeys();
	}
	
	public static void valid(){
		AssertKit.assertEqual(1, changedKeys.size());
		AssertKit.assertTrue(changedKeys.contains("group2.key2"));
	}
}
