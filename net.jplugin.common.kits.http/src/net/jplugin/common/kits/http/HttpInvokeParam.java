package net.jplugin.common.kits.http;

public class HttpInvokeParam {
	int socketTimeOut;
	int connectTimeout;
	
	private HttpInvokeParam(){}
	
	public HttpInvokeParam connectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public static HttpInvokeParam create(){
		return new HttpInvokeParam();
	}

	public HttpInvokeParam socketTimeOut(int socketTimeOut) {
		this.socketTimeOut = socketTimeOut;
		return this;
	}
	
}
