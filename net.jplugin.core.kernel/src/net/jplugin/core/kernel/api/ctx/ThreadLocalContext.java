package net.jplugin.core.kernel.api.ctx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-11 上午09:41:11
 **/

public class ThreadLocalContext {
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
		if (listeners==null || listeners.contains(ruleContextListener)) 
			return;
		else 
			addContextListener(ruleContextListener);
	}
	
	public void release(){
		if (listeners!=null){
			for (ThreadLocalContextListener l:this.listeners){
				l.released(this);
			}
		}
	}
}
