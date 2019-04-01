package net.jplugin.ext.webasic.impl.filter;

import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.ext.webasic.api.InvocationContext;

public class MethodIllegleAccessException extends RemoteExecuteException{
//	private InvocationContext ctx;

	public MethodIllegleAccessException(InvocationContext c) {
		super(RemoteExecuteException.ACCESS_FORBIDDEN,"Access Forbidden: path="+c.getServicePath()+" method="+c.getMethod().getName()+" clazz="+getObjectName(c));
//		this.ctx = c;
	}

	public MethodIllegleAccessException(String code,String message) {
		super(code,"Access Forbidden: Extro message,"+message);
//		this.ctx = c;
	}
	
	private static String getObjectName(InvocationContext c) {
		if (c.getObject()==null) return "null";
		else return c.getObject().getClass().getName();
	}
	
	

}
