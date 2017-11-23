package net.jplugin.common.kits.client;

public class InvocationParam {
	int serviceTimeOut;
	String serviceAddress;
	Boolean rpcAsync;
	ICallback rpcCallback;
	
	private InvocationParam(){}

	public static InvocationParam create(){
		return new InvocationParam();
	}

	public InvocationParam serviceTimeout(int serviceTimeOut) {
		this.serviceTimeOut = serviceTimeOut;
		return this;
	}
	
	/**
	 * 不传使用默认值
	 * @param b
	 * @return
	 */
	public InvocationParam rpcAsync(Boolean b,ICallback callback) {
		if (b!=null && !b && callback!=null){
			throw new RuntimeException("同步调用不要传入callbck");
		}
		this.rpcAsync = b;
		this.rpcCallback = callback;
		return this;
	}
	
	/**
	 * 格式  appcode#IP:端口，比如 firstapp#192.168.1.1:8090
	 * @param callback
	 * @return
	 */
	public InvocationParam serviceAddress(String aServiceAddress){
		this.serviceAddress = aServiceAddress;
		return this;
	}
	
	public int getServiceTimeOut() {
		return serviceTimeOut;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public Boolean getRpcAsync() {
		return rpcAsync;
	}

	public ICallback getRpcCallback() {
		return rpcCallback;
	}
	
	
}
