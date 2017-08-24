package net.jplugin.core.rclient.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-14 上午11:27:10
 * 
 **/

public class RemoteExecuteException extends RuntimeException {
	public static final int ERROR_NO_LOGON		= 1024;//未登录
	public static final int ERROR_PARAM_ERROR	= 1023; //参数错误
	public static final int ERROR_NO_PERMISSION	= 1022;//无权限访问
	public static final int ERROR_RISK_ACCESS	= 1021;//违背风控规则
	public static final int ERROR_NO_TENANTID 	= 1020; //多租户模式但未获取到租户ID
	public static final int ERROR_DEFAULT		= -1;//服务端异常，原因不明

	
	public static final String ACCESS_FORBIDDEN = "AccessForbidden";//这个在SSO以及APP权限判断时使用，User时不用
	

	String code;

	
	/**
	 * 
	 */
	public RemoteExecuteException(int code) {
		this(code,"");
	}

	/**
	 * 
	 */
	public RemoteExecuteException(String message) {
		super(message);
	}

	// /**
	// * @param message
	// * @param cause
	// */
	// public RemoteExecuteException(String message, Throwable cause) {
	// super(message, cause);
	// // TODO Auto-generated constructor stub
	// }
	//
	public RemoteExecuteException(int aCode, String message) {
		this(String.valueOf(aCode),message);
	}
	/**
	 * @param message
	 */
	public RemoteExecuteException(String aCode, String message) {
		super(message);
		this.code = aCode;
	}

	public RemoteExecuteException(int aCode, String message, Throwable cause) {
		this(String.valueOf(aCode),message,cause);
	}
	/**
	 * @param message
	 */
	public RemoteExecuteException(String aCode, String message, Throwable cause) {
		super(message, cause);
		this.code = aCode;
	}

	public String getCode() {
		return code;
	}

	// /**
	// * @param cause
	// */
	// public RemoteExecuteException(Throwable cause) {
	// super(cause);
	// // TODO Auto-generated constructor stub
	// }

}
