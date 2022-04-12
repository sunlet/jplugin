package net.jplugin.core.das.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-24 上午09:08:56
 **/

public class DataException extends RuntimeException {

	/**
	 * 
	 */
	public DataException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public DataException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public DataException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
