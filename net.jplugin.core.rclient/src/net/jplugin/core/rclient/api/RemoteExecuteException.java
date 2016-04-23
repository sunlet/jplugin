package net.jplugin.core.rclient.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-14 上午11:27:10
 **/

public class RemoteExecuteException extends RuntimeException {
	String code;

	/**
	 * 
	 */
	public RemoteExecuteException(String message) {
		super(message);
	}

//	/**
//	 * @param message
//	 * @param cause
//	 */
//	public RemoteExecuteException(String message, Throwable cause) {
//		super(message, cause);
//		// TODO Auto-generated constructor stub
//	}
//
	/**
	 * @param message
	 */
	public RemoteExecuteException(String aCode,String message) {
		super(message);
		this.code = aCode;
	}

	/**
	 * @param message
	 */
	public RemoteExecuteException(String aCode,String message,Throwable cause) {
		super(message,cause);
		this.code = aCode;
	}

	public String getCode() {
		return code;
	}

//	/**
//	 * @param cause
//	 */
//	public RemoteExecuteException(Throwable cause) {
//		super(cause);
//		// TODO Auto-generated constructor stub
//	}

}
