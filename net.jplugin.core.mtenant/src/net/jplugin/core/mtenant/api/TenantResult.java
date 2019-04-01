package net.jplugin.core.mtenant.api;

public  class TenantResult{
	Throwable throwable;
	Object result;
	String tenantId;
	TenantResult(String tid,Object r,Throwable t){
		this.tenantId = tid;
		this.result = r;
		this.throwable = t;
	}
	public String getTenantId() {
		return tenantId;
	}
	public Throwable getThrowable() {
		return throwable;
	}
	public Object getResult() {
		return result;
	}
}