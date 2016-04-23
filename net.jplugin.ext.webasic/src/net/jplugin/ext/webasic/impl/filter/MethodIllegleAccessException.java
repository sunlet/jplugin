package net.jplugin.ext.webasic.impl.filter;

import net.jplugin.ext.webasic.api.MethodFilterContext;

public class MethodIllegleAccessException extends RuntimeException {

	private MethodFilterContext ctx;

	public MethodIllegleAccessException(MethodFilterContext c) {
		super("Access Forbidded: path="+c.getServicePath()+" method="+c.getMethod().getName()+" clazz="+getObjectName(c));
		this.ctx = c;
		
	}

	private static String getObjectName(MethodFilterContext c) {
		if (c.getObject()==null) return "null";
		else return c.getObject().getClass().getName();
	}
	
	

}
