package test.net.jplugin.ext.webasic.dynamicmethod;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.ext.webasic.api.IDynamicService;
import net.jplugin.ext.webasic.api.InvocationContext;

public class DynamicMethodTest implements IDynamicService {

	public Object execute(InvocationContext ctx,String dynamicPath) {
		String method = dynamicPath;
		AssertKit.assertEqual(method, "test");
		String v = ctx.getRequestInfo().getContent().getParamContent().get("p1");
		AssertKit.assertEqual(v, "p1value");
		return "ooo";
	}

}
