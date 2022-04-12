package net.jplugin.ext.webasic.api;

public interface IInvocationFilter {

	public boolean before(InvocationContext ctx);
	
	public void after(InvocationContext ctx);
}
