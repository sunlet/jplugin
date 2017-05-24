package net.jplugin.core.ctx.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-9 下午01:28:40
 **/

public interface TransactionSync {
	
	public void beforeCompletion();
	
	public void afterCompletion(boolean success,Throwable th);

}
