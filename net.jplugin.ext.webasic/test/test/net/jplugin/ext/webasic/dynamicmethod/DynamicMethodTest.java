package test.net.jplugin.ext.webasic.dynamicmethod;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.ext.webasic.api.IMethodAwareService;
import net.jplugin.ext.webasic.api.MethodFilterContext;

public class DynamicMethodTest implements IMethodAwareService {

	public Object execute(MethodFilterContext ctx) {
		String method = ctx.getDynamicMethodName();
		AssertKit.assertEqual(method, "test");
		String v = ctx.getRequestInfo().getContent().getParamContent().get("p1");
		AssertKit.assertEqual(v, "p1value");
		return "ooo";
	}

}
