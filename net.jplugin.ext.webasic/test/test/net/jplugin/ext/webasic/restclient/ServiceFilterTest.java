package test.net.jplugin.ext.webasic.restclient;

import net.jplugin.ext.webasic.api.IServiceFilter;
import net.jplugin.ext.webasic.api.ServiceFilterContext;

public class ServiceFilterTest implements IServiceFilter {

	public boolean before(ServiceFilterContext ctx) {
		System.out.println("###2Before Call service:"+ctx.getServicePath()+"-"+ctx.getMethod().getName());
		return true;
	}

	public void after(ServiceFilterContext ctx) {
		System.out.println("###2After Call service:"+ctx.getServicePath()+"-"+ctx.getMethod().getName()+" return size:"+getReturnSize(ctx));
	}

	private String getReturnSize(ServiceFilterContext ctx) {
		Object result = ctx.getResult();
		if (result==null) return "NULL";
		else return ""+result.toString().length();
	}

}
