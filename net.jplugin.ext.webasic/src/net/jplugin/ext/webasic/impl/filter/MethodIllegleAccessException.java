package net.jplugin.ext.webasic.impl.filter;

import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.ext.webasic.api.InvocationContext;

public class MethodIllegleAccessException extends RemoteExecuteException{
	public static final String CODE = "AccessForbidden";
	private InvocationContext ctx;

	public MethodIllegleAccessException(InvocationContext c) {
		super(CODE,"Access Forbidden: path="+c.getServicePath()+" method="+c.getMethod().getName()+" clazz="+getObjectName(c));
		this.ctx = c;
	}

	private static String getObjectName(InvocationContext c) {
		if (c.getObject()==null) return "null";
		else return c.getObject().getClass().getName();
	}
	
	

}
