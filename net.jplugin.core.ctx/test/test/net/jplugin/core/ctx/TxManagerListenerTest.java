package test.net.jplugin.core.ctx;

import net.jplugin.core.ctx.api.ITransactionManagerListener;

public class TxManagerListenerTest implements ITransactionManagerListener{

	public void beforeBegin() {
		System.out.println("%%%%Transaction beforeBegin");
	}

	public void afterBegin() {
		System.out.println("%%%%Transaction afterBegin");
		
	}

	public void beforeCommit() {
		System.out.println("%%%%Transaction beforeCommit");
		
	}

	public void afterCommit(boolean success) {
		System.out.println("%%%%Transaction afterCommit");
	}

	public void beforeRollback() {
		System.out.println("%%%%Transaction beforeRollback");
		
	}

}
