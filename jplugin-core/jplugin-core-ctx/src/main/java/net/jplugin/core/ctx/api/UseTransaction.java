package net.jplugin.core.ctx.api;

import java.lang.annotation.*;

/**
 * <PRE>
 * 此标注用在service扩展点（BindService注册的扩展类）的方法上，实现对这个方法的事务控制。
 * 如果事务控制的类在执行过程中又调用了事务操作，则内层事务无效，合并到外层事务当中。
 *
 * 注意：只可以标注在public/protected/default类型方法上，标注在private类型方法上无法产生事务
 * </PRE>
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UseTransaction {
}
