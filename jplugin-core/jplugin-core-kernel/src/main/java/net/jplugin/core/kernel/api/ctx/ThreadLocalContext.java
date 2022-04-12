package net.jplugin.core.kernel.api.ctx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.jplugin.common.kits.client.ClientInvocationManager;
import net.jplugin.common.kits.client.InvocationParam;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-11 上午09:41:11
 **/

public class ThreadLocalContext {
	public static final String ATTR_SERVLET_REQUEST="$servlet-request";
	public static final String ATTR_SERVLET_RESPONSE="$servlet-response";
	public static final String ATTR_USING_DEF_TENANT="$using-def-tenant";

	
	HashMap<String,Object> attributes = null;
	HashMap<String,Object> sysAttribute = null;
	List<ThreadLocalContextListener> listeners = null;
	RequesterInfo requesterInfo=new RequesterInfo();

	
	public RequesterInfo getRequesterInfo() {
		return requesterInfo;
	}
	


	public void setAttribute(String key,Object val){
		if (attributes == null){
			attributes = new HashMap<String, Object>();
		}
		attributes.put(key,val);
	}
	
	public String getStringAttribute(String key){
		return (String) attributes.get(key);
	}
	
	public Object getAttribute(String key){
		return attributes==null? null:attributes.get(key);
	}
	/**
	 * @param ruleContextListener
	 */
	public void addContextListener(ThreadLocalContextListener ruleContextListener) {
		if (this.listeners==null){
			this.listeners = new ArrayList<ThreadLocalContextListener>();
		}
		this.listeners.add(ruleContextListener);
	}
	
	public void addContextListenerOnce(ThreadLocalContextListener ruleContextListener) {
		if (listeners!=null && listeners.contains(ruleContextListener)) 
			return;
		else 
			addContextListener(ruleContextListener);
	}
	
//	public Span getCurrentSpan(){
//		SpanStack ss = (SpanStack) this.getAttribute(ATTR_SPAN_STACK);
//		if (ss==null) return null;
//		else return ss.getCurrent();
//	}
	
	/**
	 * The method can't be called outside this module,so it must not be publie.
	 * The threadlocal var not be released!!!
	 */
	void release(){
		//这里的代码比较丑陋，目前由于Kit层次无法依赖Kenel进行反向注册，但是目前只能先这样
		ClientInvocationManager.INSTANCE.getAndClearParam();
		//以上
		
		if (listeners!=null){
			for (ThreadLocalContextListener l:this.listeners){
				l.released(this);
			}
		}
	}


	
}
