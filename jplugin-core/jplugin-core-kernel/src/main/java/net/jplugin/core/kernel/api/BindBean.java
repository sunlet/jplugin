package net.jplugin.core.kernel.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Please use BindServiuce， BindService can support class has no interface now.
 */
@Deprecated
public @interface BindBean {
	/**
	 * 这个标注比较特殊，id属性一定不能为空
	 * @return
	 */
	public String id();
}
