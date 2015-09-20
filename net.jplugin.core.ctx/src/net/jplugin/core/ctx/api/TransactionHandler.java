package net.jplugin.core.ctx.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-17 上午08:41:16
 **/

public interface TransactionHandler {

	/**
	 * 
	 */
	void doBegin();

	/**
	 * 
	 */
	void doRollback();

	/**
	 * 
	 */
	void doCommit();

}
