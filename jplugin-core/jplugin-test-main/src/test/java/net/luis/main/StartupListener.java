package net.luis.main;

import java.util.List;

import net.jplugin.core.kernel.api.IStartup;
import net.jplugin.core.kernel.api.PluginError;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-3-15 ����02:05:50
 **/

public class StartupListener implements IStartup{

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IStartup#startFailed(java.lang.Throwable, java.util.List)
	 */
	public void startFailed(Throwable th, List<PluginError> errors) {
		System.out.println("启动失败!!!"+errors);
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IStartup#startSuccess()
	 */
	public void startSuccess() {
		System.out.println("启动成功!!!");
	}

}
