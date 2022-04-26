package net.jplugin.core.service.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(BindServiceSet.class)
@Target(ElementType.TYPE)
public @interface BindService {
//	public String name() default "";
	public Class interfaceClass() default DefaultInterface.class;
//	public String id() default "";
	
	public static class DefaultInterface{
	}
}
