package test.net.jplugin.ext.webasic.restclient;

import net.jplugin.ext.webasic.api.IMethodFilter;
import net.jplugin.ext.webasic.api.MethodFilterContext;

public class WebCtrlFilterTest implements IMethodFilter {

	public boolean before(MethodFilterContext ctx) {
		System.out.println("$$$Before controller for:"+ctx.getServicePath()+" "+ctx.getMethod().getName());
		return true;
	}

	public void after(MethodFilterContext ctx) {
		System.out.println("$$$After controller for:"+ctx.getServicePath());
	}

}
