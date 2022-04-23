package net.jplugin.core.kernel.impl_incept;

import javassist.util.proxy.MethodHandler;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.filter.FilterManager;
import net.jplugin.common.kits.filter.IFilter;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionInterceptorContext;
import net.jplugin.core.kernel.api.IExtensionInterceptor;

import java.lang.reflect.Method;
import java.util.List;

public class TheMethodHandler implements MethodHandler, IFilter<ExtensionInterceptorContext> {
    private final Extension extension;
    private Object objectInstance;
    FilterManager<ExtensionInterceptorContext> filterManager = new FilterManager<>();


    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        ExtensionInterceptorContext ctx = new ExtensionInterceptorContext();
        ctx.init(this.extension,thisMethod,args,proceed);

        return filterManager.filter(ctx);

    }

    public TheMethodHandler(Extension ext){
        this.extension = ext;
    }

    public void setObjectInstance(Object o){
        this.objectInstance = o;
    }

    public void initFilters(List<IExtensionInterceptor> filters){
        for (IFilter f:filters){
            filterManager.addFilter(f);
        }
        filterManager.addFilter(this);
    }


    @Override
    public Object filter(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        return ctx._get_procceedMethod().invoke(this.objectInstance,ctx.getArgs());
    }
}
