package net.jplugin.core.das.route.impl;

import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.Meta;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.sf.jsqlparser.statement.Statement;

public class RouterConnectionCallContext {
	private static final String ROUTE_STATEMENT = "RouteStatement";

	public static void setStatement(Statement stmt){
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		ctx.setAttribute(ROUTE_STATEMENT, stmt);
	}
	
	public static void cleanStatment(){
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		ctx.setAttribute(ROUTE_STATEMENT, null);
	}

	public static void setMeta(Meta meta) {
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		ctx.setAttribute("ROUTE_META", meta);
	}
	
	public static Object getMeta(){
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		return ctx.getAttribute("ROUTE_META");
	}
}
