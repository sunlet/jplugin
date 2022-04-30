package net.jplugin.core.ctx.ruleincept;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.ctx.impl.DefaultRuleInvocationHandler;
import net.jplugin.core.ctx.impl.RuleInterceptor;
import net.jplugin.core.kernel.api.ExtensionInterceptorContext;
import net.jplugin.core.kernel.api.AbstractExtensionInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionInterceptor4Rule extends AbstractExtensionInterceptor {
//    ConcurrentHashMap<Method,Class> method2RuleMapping = new ConcurrentHashMap<>();

    Map<Class,RuleInterceptor.MethodMetaLocater> implClazz2Locator = new ConcurrentHashMap<>();

    @Override
    public Object filter(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        Class clazz = ctx.getExtension().getClazz();
        RuleInterceptor.MethodMetaLocater locator = getLocator(ctx.getExtension().getName(),clazz);
        RuleInterceptor.MethodMetaLocater.MethodParaInfo info = locator.findMethodParaInfo(ctx.getMethod());

        if (info!=null){
            //这种情况不再调用filter.next了。
            return new DefaultRuleInvocationHandler().invoke(null, ctx.getProceedObject(), ctx.getMethod(), ctx.getProceedMethod(), ctx.getArgs(), info.getMeta());
        }else{
            return fc.next(ctx);
        }
    }

    private RuleInterceptor.MethodMetaLocater getLocator(String name, Class clazz) {
        if (!implClazz2Locator.containsKey(clazz)){
            synchronized (clazz){
                if (!implClazz2Locator.containsKey(clazz)){
                    implClazz2Locator.put(clazz,createLocator(name,clazz));
                }
            }
        }
        return implClazz2Locator.get(clazz);
    }

    private RuleInterceptor.MethodMetaLocater createLocator(String name, Class clazz) {
        try{
            Class<?> intf = Class.forName(name);
            return new RuleInterceptor.MethodMetaLocater(intf,clazz);
        }catch(Exception e){
            throw new RuntimeException("Can't find the interface class:"+name);
        }
    }
}
