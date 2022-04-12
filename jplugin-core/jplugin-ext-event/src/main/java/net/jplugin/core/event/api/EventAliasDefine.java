package net.jplugin.core.event.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 上午08:42:38
 **/

public class EventAliasDefine {
	String eventType;
	String typeAlias;
	Class filterClass;
	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Class getFilterClass() {
		return filterClass;
	}
	public void setFilterClass(Class filterClass) {
		this.filterClass = filterClass;
	}
	public String getTypeAlias() {
		return typeAlias;
	}
	public void setTypeAlias(String typeAlias) {
		this.typeAlias = typeAlias;
	}
	
	
}
