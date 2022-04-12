package net.jplugin.core.kernel.api;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-22 下午03:07:53
 **/

public class PluginError {
	String pluginName;
	String errorMsg;
	Throwable throwable;
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Plugin="+pluginName).append(" , errorMsg = ").append(errorMsg);
		if (throwable!=null){
			sb.append(" throwable=").append(throwable.getMessage());
			sb.append(" stacktrace:");
			StringWriter sw = new StringWriter();  
            throwable.printStackTrace(new PrintWriter(sw, true));  
            String str = sw.toString();  
            sb.append(str);
		}
		return sb.toString();
	}
	
	public PluginError(String aPluginName,String msg,Throwable th){
		this.pluginName = aPluginName;
		this.errorMsg = msg;
		this.throwable = th;
	}
	
	public PluginError(String aPluginName,String msg){
		this(aPluginName,msg,null);
	}
	
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
	
	
	
}
