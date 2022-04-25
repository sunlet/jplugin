package test.net.jplugin.core.service.incept;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.ExtensionInterceptorContext;
import net.jplugin.core.kernel.api.IExtensionInterceptor;

public class ExtensionInceptTest2 implements IExtensionInterceptor {
    @Override
    public Object filter(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        return "me2-"+fc.next(ctx);
    }
}
