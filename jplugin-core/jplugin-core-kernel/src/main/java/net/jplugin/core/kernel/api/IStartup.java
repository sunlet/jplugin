package net.jplugin.core.kernel.api;

import java.util.List;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 下午01:10:25
 **/

public interface IStartup {
	public void startFailed(Throwable th,List<PluginError> errors);
	public void startSuccess();
}
