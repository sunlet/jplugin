package net.jplugin.core.ctx.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-17 上午11:13:23
 **/

public interface TransactionListener {
	public void afterCompletion(boolean success);
}
