package net.jplugin.ext.webasic.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BindServiceExport {
	public String path();
	/**
	 * id 可以通过ExtensionFactory.get(id)找到对应的对象
	 * @return
	 */
//	public String id() default "";
}