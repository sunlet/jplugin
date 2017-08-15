package net.jplugin.core.ctx.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.tuple.Tuple2;

public class JsonResult {
	public static final int JSON_FORMAT_1=1;
	public static final int JSON_FORMAT_2=2;
	public static final String JSON_FORMAT_INDICATOR="$sef";
	public static final String JSONP_FUNCTION_PARAM="callback";
	
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
		return toJson(JsonResult.JSON_FORMAT_1,null);
	}
	
	public String toJson(Tuple2<Integer, String> format) {
		return toJson(format.first,format.second);
	}
	public String toJson(int jsonFormat,String jsonp) {
		String ret = JsonResultSerialHelper.object2JsonEx(this,jsonFormat,jsonp);
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
		System.out.println(r.toJson(JSON_FORMAT_1,null));

		r = JsonResult.create();
		r.setContent("aaaaa");
		
		List<String> lll = new ArrayList<String>();
		lll.add("abc");
		lll.add("xyz");
		r.setContent(lll);
		
		System.out.println(r.toJson(JSON_FORMAT_1,null));
	}
}
