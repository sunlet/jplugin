package net.jplugin.ext.webasic.impl.restm.invoker;

import java.util.Map;

public class CallParam {
	String path;
	String operation;
	Map<String,String> paramMap;
	String result;
	
	public static CallParam create(String path,String operation,Map<String,String> map){
		CallParam cp = new CallParam();
		cp.path = path;
		cp.operation = operation;
		cp.paramMap = map;
		return cp;
	}
	
	private CallParam(){}
	
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