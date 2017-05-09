package net.jplugin.core.config.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RefConfig {
	String path();
	String defaultValue() default "";
}
