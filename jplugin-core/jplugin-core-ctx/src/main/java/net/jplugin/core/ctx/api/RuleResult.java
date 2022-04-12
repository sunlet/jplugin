package net.jplugin.core.ctx.api;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.JsonKit;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 上午10:17:24
 **/
@Deprecated
public class RuleResult {
	public static final String OK = "OK";
	private String result;
	private String message;
	private Map<String, Object> content;
	
	private RuleResult(String aResult,String aMessage,Map<String, ?> c){
		this.result = aResult;
		this.message = aMessage;
		this.content = new HashMap<String, Object>();
		if (c!=null)
			this.content.putAll(c);
	}
	public static RuleResult create(){
		return new RuleResult(OK, null,null);
	}
	public static RuleResult create(String aResult){
		return new RuleResult(aResult, null,null);
	}
	
	public static RuleResult create(String aResult,String aMessage){
		return new RuleResult(aResult, aMessage,null);
	}
	
	public static RuleResult create(String aResult,String aMessage,Map<String, ?> c){
		return new RuleResult(aResult, aMessage,c);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String success) {
		this.result = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void clearContent(){
		this.content.clear();
	}
	public Map<String, Object> getContent() {
		return content;
	}
	
	public void setContent(String key,Object val){
		this.content.put(key, val);
	}

	/**
	 * @return
	 */
	public String getJson() {
		HashMap temp = new HashMap();
		temp.put("result", result);
		temp.put("message",message);
		temp.put("content", content);
		String ret = JsonKit.object2JsonEx(temp);
		return ret;
	}
}
