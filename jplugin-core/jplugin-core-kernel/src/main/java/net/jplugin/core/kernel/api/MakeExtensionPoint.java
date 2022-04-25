package net.jplugin.core.kernel.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MakeExtensionPoint {
    public enum Type{ LIST,UNIQUE,NAMED}

    public String  name() default "";
    public Type type();
}
