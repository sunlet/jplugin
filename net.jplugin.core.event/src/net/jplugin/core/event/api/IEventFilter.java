package net.jplugin.core.event.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 上午08:44:21
 **/

public interface IEventFilter {
	public boolean match(Event e);
}
