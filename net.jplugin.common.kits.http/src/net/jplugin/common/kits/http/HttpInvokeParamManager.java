package net.jplugin.common.kits.http;

public class HttpInvokeParamManager {
	 ThreadLocal<HttpInvokeParam> param = new ThreadLocal<HttpInvokeParam>();

	 HttpInvokeParam getParam() {
		HttpInvokeParam p = param.get();
		return p;
	}
	
	 void setParam(HttpInvokeParam p){
		param.set(p);
	}
	
	 void clearParam(){
		param.set(null);
	}
	
}
