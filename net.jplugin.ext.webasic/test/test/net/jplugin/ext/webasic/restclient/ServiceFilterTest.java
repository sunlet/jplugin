package test.net.jplugin.ext.webasic.restclient;

import net.jplugin.ext.webasic.api.IInvocationFilter;
import net.jplugin.ext.webasic.api.InvocationContext;

public class ServiceFilterTest implements IInvocationFilter {

	public boolean before(InvocationContext ctx) {
		System.out.println("###2Before Call service:"+ctx.getServicePath()+"-"+ctx.getMethod().getName());
		return true;
	}

	public void after(InvocationContext ctx) {
		System.out.println("###2After Call service:"+ctx.getServicePath()+"-"+ctx.getMethod().getName()+" return size:"+getReturnSize(ctx));
	}

	private String getReturnSize(InvocationContext ctx) {
		Object result = ctx.getResult();
		if (result==null) return "NULL";
		else return ""+result.toString().length();
	}

}
