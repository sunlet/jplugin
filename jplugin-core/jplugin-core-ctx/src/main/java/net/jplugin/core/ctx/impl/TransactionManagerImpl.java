package net.jplugin.core.ctx.impl;

import net.jplugin.core.ctx.api.RollBackException;
import net.jplugin.core.ctx.api.TransactionHandler;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.ctx.api.TransactionSync;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-17 上午08:39:26
 **/
/**
 * @author Luis
 *
 */
public class TransactionManagerImpl implements TransactionManager {
	TransactionHandler[] handlers = new TransactionHandler[0]; 

	ThreadLocal<TransactionObject> txObject= new ThreadLocal<TransactionObject>(){
		@Override
		protected TransactionObject initialValue() {
			return new TransactionObject();
		}
	};

//	
//	ThreadLocal<Status> state= new ThreadLocal<Status>(){
//		@Override
//		protected Status initialValue() {
//			return Status.NOTX;
//		}
//	};
	
	private Logger logger;
	
	public void begin() {
		 //先检查状态
		 if (txObject.get().getStatus()!=Status.NOTX){
			 throw new RuntimeException("tx state not right");
		 }
		 
		 int pos = 0;
		 try{
			 for (;pos<handlers.length;pos++){
				 handlers[pos].doBegin();
			 }
		 }catch(Throwable e){
			 rollbackPreviousSilently(pos-1);
			 throw new TxRuntimeException("启动事务发生异常",e);
		 }
		 
		 //成功了再设定状态
		 txObject.get().setStatus(Status.INTX);
		 txObject.get().notifyTxBegin();
	}
	


	public void commit() {
		Status currnetState = txObject.get().getStatus();
		if (currnetState != Status.INTX && currnetState !=Status.MARKED_ROLLBACK){
			throw new RuntimeException("tx state not right");
		}
		if (currnetState == Status.MARKED_ROLLBACK){
			rollbackAllSilently();
			throw new RollBackException("tx marked roll back");
		}
		
		//执行提交前的事务同步，如果发生异常，则抛出异常
		txObject.get().notifyBeforeCompletion();
		
		
		//执行提交
		TxRuntimeException theException = null;
		int pos = 0;
		try{
			for (;pos<handlers.length;pos++){
				handlers[pos].doCommit();
			}
		}catch(Throwable e){
			commitFollowingsSilently(pos+1);
			theException = new TxRuntimeException(handlers.length>1? "事务提交异常，因为有多个事务处理器，可能不完全提交":"事务提交异常",e);
			throw theException;
		}finally{
			//无论如何都设定状态
			txObject.get().setStatus(Status.NOTX);
			txObject.get().notifyAfterCommit(theException);
		}
	}

	public void rollback() {
		Status currnetState = txObject.get().getStatus();
		
		//如果在提交时候失败不需要调用rollback，但是在写代码的时候无法避免，此时应该算成功
		if (currnetState == Status.NOTX){
			return;
		}
		
		if (currnetState != Status.INTX
				&& currnetState != Status.MARKED_ROLLBACK) {
			throw new RuntimeException("tx state not right");
		}

		TxRuntimeException theException = null;
		int pos = 0;
		try {
			for (; pos < handlers.length; pos++) {
				handlers[pos].doRollback();
			}
		} catch (Throwable e) {
			rollbackFollowingsSilently(pos);
			theException = new TxRuntimeException(handlers.length>1? "事务回滚异常，因为有多个事务处理器，可能不完全回滚":"事务回滚异常", e);
			throw theException;
		} finally {
			//无论如何都设定状态
			txObject.get().setStatus(Status.NOTX);
			txObject.get().notifyAfterRollback(theException);
		}
	}
	
	public void setRollbackOnly() {
		txObject.get().setStatus(Status.MARKED_ROLLBACK);
	}
	
	/**
	 * @param index
	 */
	private void commitFollowingsSilently(int index) {
		 for (int i=index;i<handlers.length;i++){
			 try{
				 handlers[i].doCommit();
			 }catch(Throwable th){
				 getLogger().error("提交后续事务处理器时异常",th);
			 }
		 }
	}

	/**
	 * 
	 */
	private void rollbackAllSilently() {
		rollbackPreviousSilently(handlers.length-1);
	}

	/**
	 * @param length
	 */
	private void rollbackPreviousSilently(int pos) {
		 for (int i=pos;i>=0;i--){
			 try{
				 handlers[i].doRollback();
			 }catch(Throwable th){
				 getLogger().error("回滚前驱事务处理器时异常",th);
			 }
		 }
	}

	public Status getStatus() {
		return txObject.get().getStatus();
	}
	


	/**
	 * @param pos
	 */
	private void rollbackFollowingsSilently(int index) {
		for (int i=index;i<handlers.length;i++){
			 try{
				 handlers[i].doRollback();
			 }catch(Throwable th){
				 getLogger().error("回滚后续事务处理器时异常",th);
			 }
		 }
	}
	/**
	 * @return 
	 * @return
	 */
	private  Logger getLogger() {
		if (logger==null){
			synchronized (this) {
				if (logger==null){
					logger = ServiceFactory.getService(ILogService.class).getLogger(this.getClass().getName());
				}
			}
		}
		return logger;
	}



	/* (non-Javadoc)
	 * @see net.luis.plugin.ctx.api.TransactionManager#addTransactionHandler(net.luis.plugin.ctx.api.TransactionHandler)
	 */
	public void addTransactionHandler(TransactionHandler txHandler) {
		TransactionHandler[] newhandlers = new TransactionHandler[ handlers.length  +1];
//		for (int i=0;i<handlers.length;i++){
//			newhandlers[i]=handlers[i];
//		}
//		newhandlers[newhandlers.length-1] = txHandler;
		
		//！2020-5-20修改：调整增加Handler时的顺序。后加的放在前面先执行。因为Hiberinate最后加，但是在执行过程中会调用其他的数据源的操作，所有需要先执行。
		for (int i=0;i<handlers.length;i++){
			newhandlers[i+1]=handlers[i];
		}
		newhandlers[0] = txHandler;
		
		handlers = newhandlers;
	}



	/* (non-Javadoc)
	 * @see net.luis.plugin.ctx.api.TransactionManager#addTransactionSync(net.luis.plugin.ctx.api.TransactionSync)
	 */
	public void addTransactionSync(TransactionSync s) {
		txObject.get().addSync(s);
	}



	/* (non-Javadoc)
	 * @see net.luis.plugin.ctx.api.TransactionManager#containTransactionSync(net.luis.plugin.ctx.api.TransactionSync)
	 */
	public boolean containTransactionSync(TransactionSync s) {
		return txObject.get().containsSync(s);
	}



	/* (non-Javadoc)
	 * @see net.luis.plugin.ctx.api.TransactionManager#removeTransactionSync(net.luis.plugin.ctx.api.TransactionSync)
	 */
	public void removeTransactionSync(TransactionSync s) {
		txObject.get().removeSync(s);
	}

}