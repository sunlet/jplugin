package net.jplugin.core.kernel.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * <PRE>
 * 功能：该标注使用在接口或者抽象类上，定义一个扩展点。效果类似于在所标注类所在的Plugin类的构造函数当中增加如下一行代码：
 * public Plugin extends AbstractPlugin{
 *     public Plugin(){
 *         this.addExtension(ExtensionPoint.createNamed(.....));
 *         //或者 this.addExtension(ExtensionPoint.createUnique(.....));
 *         //或者 this.addExtension(ExtensionPoint.createList(.....));
 *     }
 * }
 *
 * 注意1：扩展点的名字获取方法如下
 * 1）如果name属性设置了值，则使用name属性。
 * 2）如果name属性使用默认值（空置），则使用所标注接口/抽象类的名字（含包名）。
 *
 * 注意2：关于supportPriority属性
 * 这个属性表示所标注的类是否支持优先级。对各种类型的扩展点，优先级的含义如下：
 * 1）对于UNIQUE类型的扩展点，支持优先级时，如果在该扩展点上注册了多个扩展，系统取优先数最小的那个扩展来使用，其他扩展自动丢弃。
 * 2）对于LIST类型的扩展点，支持优先级时，系统会自动把该扩展点对应的扩展排序，按照优先数从小到大的顺序排序。
 * 3）对于NAMED类型的扩展点，支持优先级时，对应每一个Name(即Key）如果注册了多个扩展，系统自动选择优先数最小的那个扩展。（注意是在每个NAME下分别排序）
 *
 * 注意3：该标注只能使用在接口或者抽象类上，不能用在非抽象类上。
 *
 *
 * </PRE>
 */
public @interface BindExtensionPoint {
    /**
     * 指定要定义的扩展点的名字
     * @return
     */
    public String  name() default "";

    /**
     * 指定要定义的扩展点的类型，有三种类型可选择。
     * @return
     */
    public PointType type();

    /**
     * 该扩展点是否支持优先级，优先级的具体含义参照上面的JAVADOC
     * @return
     */
    public boolean supportPriority() default false;
}
