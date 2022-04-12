package net.jplugin.core.rclient.api;

import java.lang.reflect.Method;

public class ClientCallContext {
	Client client;
	Object proxy;
	Method method;
	Object[] args;
	long startTime;
	long endTime;
	
	Object returnObject;
	Throwable throwable;

	public static ClientCallContext create(Client aclient,Object aproxy, Method amethod, Object[] aargs){
		ClientCallContext c = new ClientCallContext();
		c.client = aclient;
		c.proxy = aproxy;
		c.method = amethod;
		c.args = aargs;
		c.startTime = System.currentTimeMillis();
		return c;
	}
	private ClientCallContext(){}
	public Client getClient() {
		return client;
	}
	public Object getProxy() {
		return proxy;
	}
	public Method getMethod() {
		return method;
	}
	public Object[] getArgs() {
		return args;
	}
	public long getDural(){
		if (this.endTime==0l){
			throw new RuntimeException("Can't call getDual when method not called!");
		}else return this.endTime-this.startTime;
	}
	public Object getReturnObject() {
		return returnObject;
	}
	public Throwable getThrowable() {
		return throwable;
	}
	public void setReturnObject(Object ret) {
		this.endTime = System.currentTimeMillis();
		this.returnObject = ret;
		
	}
	public void setThrowable(Throwable th) {
		this.endTime = System.currentTimeMillis();
		this.throwable = th;
	}
	public long getStartTime() {
		return startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	
	
}
