package net.jplugin.ext.webasic.impl.servicefilter;

import net.jplugin.ext.webasic.api.ServiceFilterContext;

public class ServiceIllegleAccessException extends RuntimeException {

	private ServiceFilterContext ctx;

	public ServiceIllegleAccessException(ServiceFilterContext c) {
		super("Access Forbidded: path="+c.getServicePath()+" method="+c.getMethod().getName()+" clazz="+getObjectName(c));
		this.ctx = c;
		
	}

	private static String getObjectName(ServiceFilterContext c) {
		if (c.getObject()==null) return "null";
		else return c.getObject().getClass().getName();
	}
	
	

}
