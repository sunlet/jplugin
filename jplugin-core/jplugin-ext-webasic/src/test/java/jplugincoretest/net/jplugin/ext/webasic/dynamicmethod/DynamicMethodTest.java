package jplugincoretest.net.jplugin.ext.webasic.dynamicmethod;

import java.util.Set;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.ext.webasic.api.IDynamicService;
import net.jplugin.ext.webasic.api.InvocationContext;

public class DynamicMethodTest implements IDynamicService {

	public Object execute(RequesterInfo ctx,String dynamicPath) {
		String method = dynamicPath;
		AssertKit.assertEqual(method, "test");
		String v = ctx.getContent().getParamContent().get("p1");
		AssertKit.assertEqual(v, "p1value");
		
		ctx.getContent().getParamContent().get("dataSetName");
		Set<String> names = ctx.getContent().getParamContent().keySet();
		
		return "ooo";
	}

}
