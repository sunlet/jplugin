package net.jplugin.core.ctx.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RefRuleService {
	public String name() default "";
}
