package net.jplugin.core.das.hib.impl;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import net.jplugin.core.ctx.api.TransactionHandler;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-2-17 上午11:48:52
 **/

public class TransactionHandler4Hib implements TransactionHandler {

	DASHelper helper = null;

	/**
	 * @param dasHelper
	 */
	public TransactionHandler4Hib(DASHelper h) {
		helper = h;
	}

	/*
	 * <pre>
	 * <li>如果已有session，则创建transaction。
	 * <li>在查找session的时候，如果在tx中，则同时创建tx。
	 * <li>如此确保：如果是有事物状态，则这里也是有tx的。
	 * </pre>
	 * @see net.luis.plugin.ctx.api.TransactionHandler#doBegin()
	 */
	public void doBegin() {
		ThreadLocalContext tlc = ThreadLocalContextManager.instance
				.getContext();
		Transaction tx = helper.getTxInCtx(tlc);
		if (tx != null) {
			throw new HibDasException("tx begin a second time");
		}

		// 如果已经有session，则开启事物
		helper.clearSessionAndTx(tlc);
		
		
//		Session sess = helper.getSessionInCtx(tlc);
//		if (sess != null) {
//			sess.clear();
//			tx = sess.beginTransaction();
//			helper.setTxInCtx(tlc,tx);
//		}
	}

	/*
	 * 如果能找到transaction，则提交它，session回归到自动提交模式
	 * @see net.luis.plugin.ctx.api.TransactionHandler#doCommit()
	 */
	public void doCommit() {
		ThreadLocalContext tlc = ThreadLocalContextManager.instance
				.getContext();
		try {
			Transaction tx = helper.getTxInCtx(tlc);
			if (tx != null) {
				tx.commit();
			}
		} finally {
			helper.clearSessionAndTx(tlc);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.luis.plugin.ctx.api.TransactionHandler#doRollback()
	 */
	public void doRollback() {
		ThreadLocalContext tlc = ThreadLocalContextManager.instance
		.getContext();

		try {
			Transaction tx = helper.getTxInCtx(tlc);
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			helper.clearSessionAndTx(tlc);
		}
	}

}
