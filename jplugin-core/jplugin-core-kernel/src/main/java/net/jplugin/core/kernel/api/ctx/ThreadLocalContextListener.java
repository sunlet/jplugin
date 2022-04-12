package net.jplugin.core.kernel.api.ctx;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-16 下午08:53:31
 **/

public interface ThreadLocalContextListener {
	/**
	 * 此方法不要抛异常
	 * @param rc
	 */
	public void released(ThreadLocalContext rc);
}
