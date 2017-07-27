package net.jplugin.common.kits.client;

public class InvocationParam {
	int serviceTimeOut;
	
	private InvocationParam(){}

	public static InvocationParam create(){
		return new InvocationParam();
	}

	public InvocationParam serviceTimeout(int serviceTimeOut) {
		this.serviceTimeOut = serviceTimeOut;
		return this;
	}

	public int getServiceTimeOut() {
		return serviceTimeOut;
	}
	
	
}
