package net.jplugin.ext.webasic.impl.restm.invoker;

import java.util.Map;


public class CallParam {
	public static final int CALLTYPE_STRING_PARAM=0;
	public static final int CALLTYPE_REMOTE_CALL=1;
	public static final int CALLTYPE_JSON = 2;

	public static final String REMOTE_PARATYPES_KEY = "TYPES";
	public static final String REMOTE_PARAVALUES_KEY = "PARA";
	public static final String REMOTE_EXCEPTION_PREFIX = "$RE#";
	public static final String JSON_KEY = "_@@JSON";
	
	int callType;
	String path;
	String operation;
	Map<String,String> paramMap;
	String result;
	
	public static CallParam create(int callType,String path,String operation,Map<String,String> map){
		CallParam cp = new CallParam();
		cp.callType = callType;
		cp.path = path;
		cp.operation = operation;
		cp.paramMap = map;
		return cp;
	}
	
	public static CallParam create(String path,String operation,Map<String,String> map){
		CallParam cp = new CallParam();
		cp.callType = CALLTYPE_STRING_PARAM;
		cp.path = path;
		cp.operation = operation;
		cp.paramMap = map;
		return cp;
	}
	
	private CallParam(){}
	
	public int getCallType() {
		return callType;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Map<String, String> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}