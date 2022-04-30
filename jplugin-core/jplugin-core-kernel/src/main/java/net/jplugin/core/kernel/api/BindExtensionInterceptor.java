package net.jplugin.core.kernel.api;

import java.lang.annotation.*;

/**
 * <PRE>
 * 拦截Extension方法的拦截器,标注的类需要继承基类：AbstractExtensionInterceptor。
 *
 * 拦截过滤条件分为两类：类级别过滤条件 和 属性级别过滤条件。一个方法必须同时满足两类过滤条件才进行拦截。
 * 类级别过滤条件
 *    相关属性有三个：forExtensions  forExtensionPoints forImplClasses
 *    这三个属性之间是or的关系，也就是只要被一种匹配到，就进入拦截。
 * 属性级别过滤条件
 * 	  相关属性有两个：methodNameFilter  methodAnnotationFilter
 * 	  两个属性只可以设置一个值，或者都不设置也可以（表示拦截所有方法）
 * </PRE>
 */
@Repeatable(BindExtensionInterceptorSet.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BindExtensionInterceptor {

	/**
	 * 需要拦截的Extension的id，如果有多个，请用竖线（|） 分割
	 * 注意：不支持通配符*
	 * @return
	 */
	public String forExtensions() default "";
	/**
	 * 需要拦截的ExtensionPoint的名字，如果有多个，请用竖线（|） 分割
	 * 注意：不支持通配符*
	 * @return
	 */
	public String forExtensionPoints() default "";


	/**
	 * 需要拦截的Extension实现类，如果有多个，请用竖线（|） 分割。
	 * 注意：不支持通配符*
	 * @return
	 */
	public String forImplClasses() default "";

	/**
	 * 拦截那些方法。 支持通配符和“或者”关系。
	 *
	 * 例子：
	 * 1.set*
	 * 2.setAccount|getAccount
	 * 3.getAccount|set*|put*
	 *
	 * 注意：methodAnnotationFilter 和 methodNameFilter最多只可以设置一个值。
	 * @return
	 */
	public String methodNameFilter() default "";

	/**
	 * 设置拦截带哪个标注类型的方法。
	 * 比如设置为：UseTransaction.class，表示方法上用了@UseTransaction标注就拦截
	 *
	 * 注意：methodAnnotationFilter 和 methodNameFilter最多只可以设置一个值。
	 * @return
	 */
	public Class methodAnnotationFilter() default Object.class;

}
