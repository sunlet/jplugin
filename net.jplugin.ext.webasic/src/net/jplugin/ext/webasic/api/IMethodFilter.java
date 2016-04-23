package net.jplugin.ext.webasic.api;

public interface IMethodFilter {

	public boolean before(MethodFilterContext ctx);
	
	public void after(MethodFilterContext ctx);
}
