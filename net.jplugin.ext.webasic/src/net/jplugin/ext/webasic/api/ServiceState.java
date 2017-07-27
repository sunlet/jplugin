package net.jplugin.ext.webasic.api;

import net.jplugin.ext.webasic.impl.restm.RestMethodState;

public class ServiceState {
	public static void setSuccess(boolean b){
		RestMethodState.setSuccess(b);
	}
	public static void setCode(String code){
		RestMethodState.setCode(code);
	}
	public static void setMessage(String m){
		RestMethodState.setMessage(m);
	}
}
