package net.jplugin.core.ctx.api;

import java.util.Map;

import net.jplugin.common.kits.JsonKit;

public class HttpServiceResult {
	boolean success;
	String code="0";
	String msg;
	Object result;
	
	public boolean isSuccess() {
		return success;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public Object getResult() {
		return result;
	}
	

	public static HttpServiceResult create(String json) {
		HttpServiceResult hsr = new HttpServiceResult();
		Map map = JsonKit.json2Map(json);
		if (!map.containsKey("success")){
			throw new RuntimeException("Error format json,not serialled by JsonResult:"+json);
		}
		if (map.containsKey("code")){
			return parseFromType1(map,hsr);
		}else if (map.containsKey("errno")){
			return parseFromType2(map,hsr);
		}else{
			throw new RuntimeException("Error format json,not serialled by JsonResult:"+json);
		}
	}

	

	private static HttpServiceResult parseFromType1(Map map, HttpServiceResult jr) {
		jr.success = (boolean) (map.get("success"));
		jr.code = ((String) map.get("code"));
		jr.msg = (String)map.get("msg");
		Object content = map.get("content");
		if (content!=null){
			Object result = ((Map)content).get("result");
			if (result!=null) 
				jr.result = result;
		}
		return jr;
	}

	private static HttpServiceResult parseFromType2(Map map, HttpServiceResult jr) {
		jr.success = (boolean) (map.get("success"));
		jr.code = ((String) map.get("errno").toString());
		jr.result = map.get("data");
		jr.msg = (String)map.get("errmsg");
		return jr;
	}
}
