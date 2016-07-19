package net.jplugin.ext.webasic.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
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
	String name() default "";
	/**
	 * API控制级别
	 * @return
	 */
	RL restrictLevel() default RL.NONE;
	CT callerType();
}
