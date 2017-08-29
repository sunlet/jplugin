package net.jplugin.core.ctx.impl;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.ctx.api.TransactionHandler;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.ctx.api.TransactionSync;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-17 上午11:17:32
 **/

public class TransactionManagerAdaptor implements TransactionManager{
	private TransactionManager inner;

	public TransactionManagerAdaptor(){
		this.inner = new TransactionManagerImpl();
	}
	
	public static boolean isLogTx;
	public static void init(){
		isLogTx = "true".equalsIgnoreCase(ConfigFactory.getStringConfig("platform.log-tx-exec"));
		PluginEnvirement.INSTANCE.getStartLogger().log("platform.log-tx-exec value is:"+isLogTx);
	}

	public void begin(){
		this.begin("");
	}
	public void begin(String desc) {
		try{
			TxMgrListenerManager.beforeBegin();
			inner.begin();
			TxMgrListenerManager.afterBegin();
			if(isLogTx)
				RuleLoggerHelper.dolog("tx begin success -"+desc);
		}catch(Exception e){
			if(isLogTx)
				RuleLoggerHelper.dolog("tx begin error. -"+desc,e);
			rethrow(e);
		}
	}
	public void commit() {
		this.commit("");
	}
	public void commit(String desc) {
		try{
			TxMgrListenerManager.beforeCommit();
			inner.commit();
			TxMgrListenerManager.afterCommit(true);
			if(isLogTx)
				RuleLoggerHelper.dolog("tx commit success. -"+desc);
		}catch(Exception e){
			TxMgrListenerManager.afterCommit(false);
			if(isLogTx)
				RuleLoggerHelper.dolog("tx commit error. -"+desc,e);
			rethrow(e);
		}
	}

	public Status getStatus() {
		return inner.getStatus();
	}

	public void rollback() {
		this.rollback("");
	}
	public void rollback(String desc) {
		try{
			TxMgrListenerManager.beforeRollback();
			inner.rollback();
			if(isLogTx)
				RuleLoggerHelper.dolog("tx rollback success. -"+desc);
		}catch(Exception e){
			if(isLogTx)
				RuleLoggerHelper.dolog("tx rollback error. -"+desc,e);
			rethrow(e);
		}
	}

	public void setRollbackOnly() {
		try{
			inner.setRollbackOnly();
			if(isLogTx)
				RuleLoggerHelper.dolog("tx rollbackonly success. ");
		}catch(Exception e){
			if(isLogTx)
				RuleLoggerHelper.dolog("tx rollbackonly error. ",e);
			rethrow(e);
		}
	}
	/**
	 * @param e
	 */
	private void rethrow(Exception e) {
		if (e instanceof RuntimeException)
			throw (RuntimeException)e;
		else 
			throw new RuntimeException(e);
	}

	public void addTransactionHandler(TransactionHandler txHandler) {
		inner.addTransactionHandler(txHandler);
	}

	public void addTransactionSync(TransactionSync s) {
		inner.addTransactionSync(s);
	}

	public boolean containTransactionSync(TransactionSync s) {
		return inner.containTransactionSync(s);
	}

	public void removeTransactionSync(TransactionSync s) {
		inner.removeTransactionSync(s);
	}
}