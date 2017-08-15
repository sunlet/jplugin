package net.jplugin.core.rclient.handler;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.JsonKit;

public class JsonResult4Client {
	public static final String JSON_FORMAT_INDICATOR="$sef";
	
	boolean success;
	String code="0";
	String msg;
	Object content;
	Map<String,String> props;

	public JsonResult4Client() {
	}
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
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

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String toJson() {
		String ret = JsonKit.object2JsonEx(this);
		return ret;
	}
	
	public String _getProp(String key){
		if (this.props==null) return null;
		return this.props.get(key);
	}
	public void _setProp(String key,String val){
		if (this.props==null) this.props = new HashMap<String, String>();
		this.props.put(key, val);
	}

}