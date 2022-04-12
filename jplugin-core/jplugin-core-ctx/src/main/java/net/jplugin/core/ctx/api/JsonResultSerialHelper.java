package net.jplugin.core.ctx.api;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.rclient.api.RemoteExecuteException;

public class JsonResultSerialHelper {

	public static String object2JsonEx(JsonResult jsonResult, int jsonFormat, String jsonp) {
		//判断format
		String result;
		switch(jsonFormat){
		case JsonResult.JSON_FORMAT_1:
			result = serialFormat1(jsonResult);
			break;
		case JsonResult.JSON_FORMAT_2:
			result = serialFormat2(jsonResult);
			break;
		case JsonResult.JSON_FORMAT_3:
			result = serialFormat3(jsonResult);
			break;
		default:
			throw new RuntimeException("Error json format indicator:"+jsonFormat);
		}
		//判断jsonp
		if (jsonp!=null){
			return convertToJsonp(result,jsonp);
		}else
			return result;
	}

	private static String convertToJsonp(String result, String jsonp) {
		StringBuffer sb = new StringBuffer(jsonp);
		sb.append("(").append(result).append(")");
		return sb.toString();
	}

	private static String serialFormat3(JsonResult jr) {
		return serialFormatCommon(jr,"code","msg","success","data");
	}
	
	private static String serialFormatCommon(JsonResult jr,String errorNoKey,String errorMsgKey,String successKey,String dataKey) {
		Map m = new HashMap();
		m.put(errorNoKey, convertErrNo(jr.code));
		m.put(errorMsgKey, jr.msg);
		m.put(successKey, jr.success);
		Object content = jr.content;
		
		//目前的实现，content是根据类型1的映射，所以过程相对复杂，只处理result节点
		if (content!=null){
			if (content instanceof Map){
				Object result = ((Map)content).get("result");
				if (result!=null){
					if (result instanceof JsonRootMap){
						//检查后作为根部元素
						if ( ((Map)result).containsKey(errorNoKey))
							throw new RuntimeException("Method can't return a map with key:"+errorNoKey);
						if ( ((Map)result).containsKey(errorMsgKey))
							throw new RuntimeException("Method can't return a map with key:" + errorMsgKey);
						if ( ((Map)result).containsKey(successKey))
							throw new RuntimeException("Method can't return a map with key:"+successKey);
						m.putAll((Map)result);
					}else{
						//作为data节点
						m.put(dataKey, result);
					}
				}
			}else{
				//类型校验，目前类型2的话到这里一定是map，因为是类型1的结构
				throw new RuntimeException("In format2,the content must be Map type now");
			}
		}
		return JsonKit.object2Json(m);
	}

	/**
	 * 目前先独立实现，未来可以切换到调用serialFormatCommon来实现。
	 * 因为这个方法先实现，保持稳定性，不修改了。
	 * @param jr
	 * @return
	 */
	private static String serialFormat2(JsonResult jr) {
		Map m = new HashMap();
		m.put("errno", convertErrNo(jr.code));
		m.put("errmsg", jr.msg);
		m.put("success", jr.success);
		Object content = jr.content;
		
		//目前的实现，content是根据类型1的映射，所以过程相对复杂，只处理result节点
		if (content!=null){
			if (content instanceof Map){
				Object result = ((Map)content).get("result");
				if (result!=null){
					if (result instanceof JsonRootMap){
						//检查后作为根部元素
						if ( ((Map)result).containsKey("errno"))
							throw new RuntimeException("Method can't return a map with key:errno");
						if ( ((Map)result).containsKey("errmsg"))
							throw new RuntimeException("Method can't return a map with key:errmsg");
						if ( ((Map)result).containsKey("success"))
							throw new RuntimeException("Method can't return a map with key:success");
						m.putAll((Map)result);
					}else{
						//作为data节点
						m.put("data", result);
					}
				}
			}else{
				//类型校验，目前类型2的话到这里一定是map，因为是类型1的结构
				throw new RuntimeException("In format2,the content must be Map type now");
			}
		}
		return JsonKit.object2Json(m);
	}
	private static Object convertErrNo(String code) {
		if (RemoteExecuteException.ACCESS_FORBIDDEN.equals(code)){
			//forbidden 类型固定转换一下
			return RemoteExecuteException.ERROR_NO_LOGON;
		}else{
			//空 或者 非数字，转换为-1。其他做类型转换即可。
			if (code==null || !StringKit.isNumAllowNig(code))
				return RemoteExecuteException.ERROR_DEFAULT;
			else
				return Integer.parseInt(code);
		}
	}

	private static String serialFormat1(JsonResult jr) {
		return JsonKit.object2Json(jr);
	}
}
