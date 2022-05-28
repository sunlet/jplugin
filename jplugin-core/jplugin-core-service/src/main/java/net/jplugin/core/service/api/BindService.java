package net.jplugin.core.service.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(BindServiceSet.class)
@Target(ElementType.TYPE)
/**
 * <PRE>
 * 功能：使用所标注的类注册一个扩展，扩展点为accessClass的类名。
 * 如果对应的服务存在对应的接口，accessClass设为所标注的类实现的接口。
 *
 *     @BindService ( accessClass=IMYServiceInterface.class )
 *     public class MyService implements IMyServiceInterface{
 *     }
 *
 * 如果对应的服务不存在接口，accessClass设置为该类本身。
 *
 *      @BindService (accessClass = MyService.class)
 *      public class MyService implements IMyServiceInterface{
 *      }
 *
 * 旧版本的intfClass属性已经去除，修改为accessClass属性。
 * </PRE>
 *
 */
public @interface BindService {
//	public String name() default "";
	public Class accessClass() default BindService.class;
//	public String id() default "";
//
//	public static class DefaultInterface{
//	}
}

