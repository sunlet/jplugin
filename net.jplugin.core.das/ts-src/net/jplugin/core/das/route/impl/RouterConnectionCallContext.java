package net.jplugin.core.das.route.impl;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.sf.jsqlparser.statement.Statement;

public class RouterConnectionCallContext {
	private static final String ROUTE_STATEMENT = "RouteStatement";

	public void setStatement(Statement stmt){
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		ctx.setAttribute(ROUTE_STATEMENT, stmt);
	}
	
	public void cleanStatment(){
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		ctx.setAttribute(ROUTE_STATEMENT, null);
	}
}
