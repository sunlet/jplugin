package net.jplugin.core.rclient.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-14 上午11:27:10
 * 
<pre>
1.  -1000 参数校验异常
2.  -1001 未登录
3.  -1002 无权限访问  
4.  -1003 违反风控规则
5.  -1到-999，系统自定义的错误返回码 
6.  0  默认的正常的返回码
7.  >1 自定义的正常返回码
</pre>
 **/

public class RemoteExecuteException extends RuntimeException {
	public static final String PARAM_ERROR="-1000"; //参数错误
	public static final String NO_LOGON="-1001";//未登录
	public static final String NO_PERMISSION="-1002";//无权限访问
	public static final String NO_RISK="-1003";//违背风控规则
	public static final String ACCESS_FORBIDDEN = "AccessForbidden";//这个在SSO以及APP权限判断时使用，User时不用

	String code;

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
	/**
	 * @param message
	 */
	public RemoteExecuteException(String aCode, String message) {
		super(message);
		this.code = aCode;
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
