package net.jplugin.core.kernel.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DefineExtensionPoint {

    public String  name() default "";
    public PointType type();
    public boolean supportPriority() default false;
}
