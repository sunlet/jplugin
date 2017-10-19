package net.jplugin.core.ctx.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(BindRuleServiceSet.class)
@Target(ElementType.TYPE)
public @interface BindRuleService {
	public String name() default "";
	public Class interfaceClass() default DefaultInterface.class;
	
	public static class DefaultInterface{
	}
}
