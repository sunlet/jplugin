package net.jplugin.core.das.mybatis.impl.sess;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextListener;

public class SessionClearContextListener implements ThreadLocalContextListener {

	@Override
	public void released(ThreadLocalContext rc) {
		MybatisSessionManager.releaseSessions(rc);
	}

}
