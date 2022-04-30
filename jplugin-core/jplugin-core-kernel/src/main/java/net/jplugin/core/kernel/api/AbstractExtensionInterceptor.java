package net.jplugin.core.kernel.api;

import net.jplugin.common.kits.StringMatcher;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.filter.IFilter;

/**
 * <PRE>
 * 此类为BindExtensionInterceptor标注的类需要继承的基类。
 * 如果想在方法执行前拦截，请重载 beforeExecute
 * 如果想在方法执行后拦截，请重载 afterExecute
 * 如果想拦截方法的执行，修改方法的返回值，统计方法的执行时长等，请重载execute
 *
 * 注意：
 * 1.此类不是线程安全的，所有拦截的Extension的所有方法都经过这个类。
 * 2.通过ctx （为ExtensionInterceptorContext类型） 可以获取当前拦截的扩展点、扩展、方法、参数 等上下文信息。
 * </PRE>
 */
public abstract class AbstractExtensionInterceptor implements IFilter <ExtensionInterceptorContext>{

    IMethodFilter methodMatcher;

    public void setMethodFilter(IMethodFilter filter){
        this.methodMatcher = filter;
    }

    /**
     * 非特殊情況，不要重载这个方法，请重载 beforeExecute execute afterExecute 方法。
     * 当需要忽略扩展当中配置的methodFilter的时候，直接重载这个方法，性能最好。
     * 在这个方法当中需要用fc.next(ctx)继续后续的处理。
     * @param fc
     * @param ctx
     * @return
     * @throws Throwable
     */
    @Override
    public Object filter(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        if (methodMatcher==null || methodMatcher.match(ctx.getMethod())){
            beforeExecute(ctx);
            try{
                Object result = execute(fc,ctx);
                afterExecute(ctx,result);
                return result;
            }catch(Throwable th){
                afterExecute(ctx,th);
                throw th;
            }
        }else {
            return fc.next(ctx);
        }
    }

    /**
     * 方法执行前插入行为
     * @param ctx
     */
    protected void beforeExecute(ExtensionInterceptorContext ctx) {
    }

    /**
     * 当需要考虑扩展当中配置的methodFilter的时候，并且需要拦截整个方法，重载这个方法。
     * 在这个方法当中需要用super.execute(...) 或者 fc.next(ctx)继续后续的处理。
     * @param fc
     * @param ctx
     * @return
     * @throws Throwable
     */
    protected Object execute(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
       return fc.next(ctx);
    }



    /**
     * 方法正常执行完（没有抛异常）后插入行为
     * @param ctx
     * @param result
     */
    protected void afterExecute(ExtensionInterceptorContext ctx,Object result) {
    }

    /**
     * 方法抛出异常时，插入行为
     * @param ctx
     * @param th
     */
    protected void afterExecute(ExtensionInterceptorContext ctx,Throwable th) {
    }

}
