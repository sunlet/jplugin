package net.jplugin.core.ctx.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BindRuleService {
	public String name() default "";
	public Class interfaceClass() default DefaultInterface.class;
	
	public static class DefaultInterface{
	}
}
