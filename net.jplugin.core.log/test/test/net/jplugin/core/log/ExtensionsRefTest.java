package test.net.jplugin.core.log;

import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.IExecutorFilter;
import net.jplugin.core.kernel.api.Initializable;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.kernel.api.RefExtensionMap;
import net.jplugin.core.kernel.api.RefExtensions;

public class ExtensionsRefTest extends RefAnnotationSupport implements Initializable{

	@RefExtensions(pointTo=net.jplugin.core.kernel.Plugin.EP_EXE_RUN_INIT_FILTER)
	List<IExecutorFilter> filters;
	
	@RefExtensionMap(pointTo="EP_RULE_SERVICE")
	Map<String,IExecutorFilter> map;
	
	public void test(){
		AssertKit.assertTrue(filters.size()>0);
		AssertKit.assertTrue(map.size()>0);
	}

	public void initialize() {
		System.out.println("initing...");
	}
	
}
