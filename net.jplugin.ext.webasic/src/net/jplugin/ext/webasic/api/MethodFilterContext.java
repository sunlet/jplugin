package net.jplugin.ext.webasic.api;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodFilterContext {
	String servicePath;
	Method method;
	Object object;
	Object[] args;
	Object result;
	Throwable th;
	Map<String,Object> attributes;
	
	public  MethodFilterContext(String p,Object o,Method m,Object[] a){
		this.servicePath = p;
		this.method = m;
		this.object = o;
		this.args = a;
	}
	
	public String getServicePath() {
		return servicePath;
	}
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public Throwable getTh() {
		return th;
	}
	public void setTh(Throwable th) {
		this.th = th;
	}
	
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public void setAttribute(String k,Object v){
		if (attributes==null) attributes = new HashMap<String, Object>();
		attributes.put(k, v);
	}
	@Deprecated
	public void sddAttribute(String k,Object v){
		setAttribute(k, v);
	}
	public Object getAttribute(String k){
		if (attributes==null) return null;
		return attributes.get(k);
	}
}
