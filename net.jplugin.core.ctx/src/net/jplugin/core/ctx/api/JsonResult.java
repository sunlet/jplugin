package net.jplugin.core.ctx.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.JsonKit;

public class JsonResult {
	boolean success;
	String code="0";
	String msg;
	Object content;
	Map<String,String> props;

	private JsonResult() {
	}

	public static JsonResult create() {
		return create(true);
	}

	public static JsonResult create(boolean aSuccess) {
		JsonResult s = new JsonResult();
		s.success = aSuccess;
		return s;
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

	public static void main(String[] args) {
		JsonResult r = JsonResult.create(false);
		r.create(false);
		System.out.println(r.toJson());

		r = JsonResult.create();
		r.setContent("aaaaa");
		
		List<String> lll = new ArrayList<String>();
		lll.add("abc");
		lll.add("xyz");
		r.setContent(lll);
		
		System.out.println(r.toJson());
	}
}
