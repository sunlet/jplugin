package net.jplugin.core.config.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RefConfig {
	String path();
	String defaultValue() default "";
	boolean autoRefresh() default false;
}
