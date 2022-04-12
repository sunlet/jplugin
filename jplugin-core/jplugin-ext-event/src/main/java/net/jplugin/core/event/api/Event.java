package net.jplugin.core.event.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午03:24:16
 **/

public abstract class Event {
	private long id;
	String type;
	
	public Event(String aType){
		this.type = aType;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public final String getType() {
		return type;
	}
}
