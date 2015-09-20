package net.jplugin.core.ctx.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-14 上午09:53:17
 **/

public class RollBackException extends RuntimeException {

	/**
	 * 
	 */
	public RollBackException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RollBackException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public RollBackException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public RollBackException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
