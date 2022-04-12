package net.jplugin.core.kernel.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拦截Extension方法的拦截器
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BindExtensionInterceptor {
	/**
	 * 需要拦截的Extension的id，如果有多个，请用逗号分割
	 * @return
	 */
	public String forExtensions() default "";
	/**
	 * 需要拦截的ExtensionPoint的名字，如果有多个，请用逗号分割
	 * @return
	 */
	public String forExtensionPoints() default "";

	/**
	 * 拦截那些方法。 支持通配符，比如：get*  或者   setAccount|getAccount 等。
	 * @return
	 */
	public String methodFilter() default "";
}
