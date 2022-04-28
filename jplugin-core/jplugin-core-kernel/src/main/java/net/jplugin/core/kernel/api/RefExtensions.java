package net.jplugin.core.kernel.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

/**
 *  功能：该标注用在类的Field上，表示该Field引用一个扩展点的扩展实例列表。该Field的类型必须是java.util.List，请带泛型使用。
 *
 *  注意1：对应的扩展点必须是PointType.LIST，注意不要用在PointType.NAMED或PointType.UNIQUE类型的扩展点上。
 *
 *  注意2：对应扩展点的名字获取方法如下：
 *  1）如果pointTo有值，则使用对应的名字作为扩展点。
 *  2）如果pointTo为默认值（空字符串），系统使用 所标注的Field 对应的类名字（含包名）作为扩展点名字。
 *
 *  例子： 下面标注，效果等同于 PluginEnvirement.getInstance().getExtensionMap(IService1.class.getName(),IService1.class)
 *       @RefExtension
 *       Map<String,IService1> service;
 *
 *  例子： 下面标注，效果等同于 PluginEnvirement.getInstance().getExtensionMap("mypoint",IService1.class)
 *       @RefExtension(pointTo="mypoint")
 *       Map<String,IService1> service;
 *
 */
public @interface RefExtensions{
	/**
	 * 指定对应的扩展点名称，默认值(空字符串）表示让系统自动推理，推理规则参照上面JAVADOC
	 * @return
	 */
	String pointTo() default "";
}
