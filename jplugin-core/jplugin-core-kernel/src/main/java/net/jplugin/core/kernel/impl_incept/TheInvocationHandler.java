package net.jplugin.core.kernel.impl_incept;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.filter.FilterManager;
import net.jplugin.common.kits.filter.IFilter;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionInterceptorContext;
import net.jplugin.core.kernel.api.IExtensionInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class TheInvocationHandler implements InvocationHandler, IInstanceLevelInfo,IFilter<ExtensionInterceptorContext> {
    private final Extension extension;
    private Object implObject;
    FilterManager<ExtensionInterceptorContext> filterManager = new FilterManager<>();

    public TheInvocationHandler(Extension ext,Object aImplObject){
        this.extension = ext;
        this.implObject = aImplObject;
    }

    public void initFilters(List<IExtensionInterceptor> filters){
        for (IFilter f:filters){
            filterManager.addFilter(f);
        }
        filterManager.addFilter(this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ExtensionInterceptorContext ctx = new ExtensionInterceptorContext();
//        ctx.init(this.extension,method,args);

        //当前方法和继续执行需要的方法是同一个。
        ctx.init(method,args,this,method);

        return filterManager.filter(ctx);
    }

    @Override
    public Object filter(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        //下面的this.implObject 和 ctx.getProceedObject 是同一个对象！！！
//        AssertKit.assertEqual(this.implObject,ctx.getProceedObject());

        return ctx.getMethod().invoke(this.implObject,ctx.getArgs());
    }


    //下面两个方法实现IInstanceLevelInfo，这样做为了让Context里面能获取到相关信息
    @Override
    public Extension getExtension() {
        return this.extension;
    }

    @Override
    public Object getProceedObject() {
        return this.implObject;
    }



}
