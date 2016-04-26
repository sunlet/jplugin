package test.net.jplugin.ext.webasic.restclient;

import net.jplugin.ext.webasic.api.IMethodFilter;
import net.jplugin.ext.webasic.api.MethodFilterContext;

public class ServiceFilterTest implements IMethodFilter {

	public boolean before(MethodFilterContext ctx) {
		System.out.println("###2Before Call service:"+ctx.getServicePath()+"-"+ctx.getMethod().getName());
		return true;
	}

	public void after(MethodFilterContext ctx) {
		System.out.println("###2After Call service:"+ctx.getServicePath()+"-"+ctx.getMethod().getName()+" return size:"+getReturnSize(ctx));
	}

	private String getReturnSize(MethodFilterContext ctx) {
		Object result = ctx.getResult();
		if (result==null) return "NULL";
		else return ""+result.toString().length();
	}

}
