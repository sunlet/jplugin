package net.jplugin.core.event.api;

import java.util.Vector;


/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午03:25:31
 **/

public abstract class Channel {
	public enum ChannelType{SYNC,POST_MEMORY,POST_PERSISTENCE}
	protected Vector<EventConsumer> consumers = new Vector<EventConsumer>();
	/**
	 * @param e
	 */
	public abstract void sendEvent(Event e);
	
	public abstract ChannelType getChannelType();

	/**
	 * @param c
	 */
	public void addConsumer(EventConsumer c) {
		this.consumers.add(c);
	}
}
