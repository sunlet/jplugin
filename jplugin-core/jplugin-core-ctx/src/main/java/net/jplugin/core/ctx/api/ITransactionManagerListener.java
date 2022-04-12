package net.jplugin.core.ctx.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-17 上午11:13:23
 **/

public interface ITransactionManagerListener {
	public void beforeBegin();
	public void afterBegin();
	public void beforeCommit();
	public void afterCommit(boolean success);
	public void beforeRollback();
}
