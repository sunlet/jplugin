package net.luis.main;

import java.util.List;

import net.jplugin.core.kernel.api.IStartup;
import net.jplugin.core.kernel.api.PluginError;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-15 下午02:05:50
 **/

public class StartupListener implements IStartup{

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IStartup#startFailed(java.lang.Throwable, java.util.List)
	 */
	public void startFailed(Throwable th, List<PluginError> errors) {
		System.out.println("启动失败了!!!"+errors);
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IStartup#startSuccess()
	 */
	public void startSuccess() {
		System.out.println("启动成功了!!!");
	}

}
