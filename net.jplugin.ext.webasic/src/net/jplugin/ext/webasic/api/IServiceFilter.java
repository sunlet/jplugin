package net.jplugin.ext.webasic.api;

public interface IServiceFilter {

	public boolean before(ServiceFilterContext ctx);
	
	public void after(ServiceFilterContext ctx);
}
