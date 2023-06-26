package net.jplugin.core.ctx.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(BindRuleMethodInterceptorSet.class)
@Target(ElementType.TYPE)
public @interface BindRuleMethodInterceptor {
	public String applyTo();
	public int sequence() default 10;//默认不要为0，0优先级太高了
//	public String id() default "";
}
