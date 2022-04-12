package net.jplugin.core.das.mybatis.impl.sess;

import net.jplugin.core.ctx.api.ITransactionManagerListener;

public class MybatisTransactionManagerListener implements ITransactionManagerListener {

	@Override
	public void beforeBegin() {
		//这里使用clearSessions()会报错，猜测因为begin过程中会导致链接被释放，如果session保留的话
		//会导致后面的session执行报数据库链接关闭错。
		//本质原因是mybatis目前还是持有Connection的引用的，如果不持有就好了,未来考虑改进一下。
		MybatisSessionManager.releaseSessions();
	}

	@Override
	public void afterBegin() {
	}

	@Override
	public void beforeCommit() {
		MybatisSessionManager.releaseSessions();
	}

	@Override
	public void afterCommit(boolean success) {
	}

	@Override
	public void beforeRollback() {
		MybatisSessionManager.releaseSessions();
	}

}
