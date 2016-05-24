package net.jplugin.ext.webasic.api;

public @interface JPluginApi {
	/**
	 * 调用者类型：CallerType
	 */
	public enum CT{APP,USER}
	/**
	 * 限制级别：RestrictLevel
	 */
	public enum RL{NONE,TK,AUTH}
	/**
	 * API名称
	 * @return
	 */
	String name();
	/**
	 * API控制级别
	 * @return
	 */
	RL restrictLevel() default RL.NONE;
	CT callerType();
}
