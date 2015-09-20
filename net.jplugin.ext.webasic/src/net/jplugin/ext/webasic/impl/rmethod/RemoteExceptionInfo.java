package net.jplugin.ext.webasic.impl.rmethod;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-13 上午09:07:46
 **/

public class RemoteExceptionInfo {
	String exceptionMessage;

	/**
	 * @param message
	 */
	public RemoteExceptionInfo(String message) {
		this.exceptionMessage = message;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
