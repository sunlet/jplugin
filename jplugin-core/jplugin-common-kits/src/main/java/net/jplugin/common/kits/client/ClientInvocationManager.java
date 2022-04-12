package net.jplugin.common.kits.client;

public class ClientInvocationManager {
	public static ClientInvocationManager INSTANCE = new ClientInvocationManager();
	ThreadLocal<InvocationParam> param = new ThreadLocal<InvocationParam>();
	
	public void setParam(InvocationParam p){
		param.set(p);
	}
	public InvocationParam getParam() {
		return param.get();
	}
	public InvocationParam getAndClearParam() {
		InvocationParam p = param.get();
		if (p!=null) 
			param.set(null);
		return p;
	}
	
}
