package net.jplugin.core.kernel.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该标注用在类上，用来在系统启动完毕的时候执行一些动作。这个类需要实现IStartup接口。
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BindStartup{
//	String id() default "";
}
