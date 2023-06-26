package jplugincoretest.net.jplugin.ext.webasic.restclient;

import net.jplugin.ext.webasic.api.IInvocationFilter;
import net.jplugin.ext.webasic.api.InvocationContext;

public class WebCtrlFilterTest implements IInvocationFilter {

	public boolean before(InvocationContext ctx) {
		System.out.println("$$$Before controller for:"+ctx.getServicePath()+" "+ctx.getMethod().getName());
		return true;
	}

	public void after(InvocationContext ctx) {
		System.out.println("$$$After controller for:"+ctx.getServicePath());
	}

}
