package net.jplugin.ext.webasic.impl;

import java.util.HashMap;

import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.core.service.api.ServiceFactory;

public class RemoteExceptionKits {

	public static class RemoteExceptionInfo{
		String code;
		String msg;
		public RemoteExceptionInfo(String errCode, String errMsg) {
			this.code = errCode;
			this.msg = errMsg;
		}
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
	}

	public static RemoteExceptionInfo getExceptionInfo(Throwable targetEx) {
		String errCode="";
		String errMsg = "";
		if (targetEx instanceof RemoteExecuteException){
			errMsg = (((RemoteExecuteException)targetEx).getMessage());//get message
			errCode = (((RemoteExecuteException)targetEx).getCode());//get code
		}else{
			errMsg = (targetEx.getMessage());
			errCode = "-1";
		}
		return new RemoteExceptionInfo(errCode,errMsg);
	}
}
