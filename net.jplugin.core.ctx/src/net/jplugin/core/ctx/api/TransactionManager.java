package net.jplugin.core.ctx.api;


/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-12 上午10:39:12
 **/

public interface TransactionManager {
	enum Status{NOTX,INTX,MARKED_ROLLBACK}
	/**
	 * 开启事物，如果重复开启，则异常
	 */
	public void begin();

	/**
	 * 提交事务
	 */
	public void commit();

	/**
	 * 回滚事物
	 */
	public void rollback();
	
	/**
	 * 设置回滚
	 */
	public void setRollbackOnly();
	
	public Status getStatus();

	/**
	 * @param txHandler
	 */
	public void addTransactionHandler(TransactionHandler txHandler);
	
	/**
	 * 在事物当中才可以调用此方法
	 * @param s
	 */
	public boolean containTransactionSync(TransactionSync s);
	
	/**
	 * 在事物当中才可以调用此方法
	 * @param s
	 */
	public void addTransactionSync(TransactionSync s);
	
	/**
	 * 在事物当中才可以调用此方法
	 * @param s
	 */
	public void removeTransactionSync(TransactionSync s);

}
