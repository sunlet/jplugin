package net.jplugin.core.service.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该标注用在Field上，表示引用Service扩展点(扩展点名字为net.jplugin.core.service.Plugin.EP_SERVICE)下的一个对应名字（name）的扩展。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RefService {
	/**
	 * 指定引用的Service的名字，默认值（空字符串）表示使用对应Field的Class的名字（含包名）
	 *
	 * @return
	 */
	public String name() default "";
}
