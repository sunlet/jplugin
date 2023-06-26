package net.jplugin.core.kernel.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * <PRE>
 *功能：
 * 使用所标注的类为实现类（下面称“该类”）注册一个扩展。
 * 如果设置了pointTo属性，则在pointTo属性为名字的扩展点上注册扩展。
 * 如果没有设置pointTo属性，系统会自动推测扩展点，推测的算法如下：
 *   1）系统列出该类的所有接口（仅包括该类直接实现的接口，通过父类实现的接口不算）的名字（含包名），加入候选集。
 *   2）如果该类的直接父类是一个抽象类，则把父类的名字加入候选集。
 *   3）自动逐个判断所有候选集的名字，看看有存在对应名字的扩展点：
 *      如果有一个名字对应到扩展点，则用这个名字作为扩展点去注册扩展。
 *      如果所有候选集对应不到扩展点，或者能够对应到多个扩展点。则系统抛出异常。
 *
 * 这个标注的效果类似于在对应的Plugin类的构造函数中增加代码：
 * 		   ExtensionXXXHelper.addXXXXExtension(....)
 *         //或  this.addExtension(....);
 * </PRE>
 */
public @interface BindExtension {
	/**
	 * 指定要在哪个扩展点上进行扩展，默认值（空字符串）表示让系统自动推测扩展点名字。
	 * @return
	 */
	public String pointTo() default "";

	/**
	 * 为这个扩展设置一个名字。注意：对于NAMED类型扩展点需要设置扩展点的名字，对于其他类型的扩展点不要设置名字。
	 * @return
	 */
	public String name() default "";
//	public String id() default "";
//	public short priority() default 0;
}
