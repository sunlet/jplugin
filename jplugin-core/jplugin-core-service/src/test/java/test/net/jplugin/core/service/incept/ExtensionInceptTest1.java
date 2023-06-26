package test.net.jplugin.core.service.incept;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.ExtensionInterceptorContext;
import net.jplugin.core.kernel.api.AbstractExtensionInterceptor;

public class ExtensionInceptTest1 extends AbstractExtensionInterceptor {
    @Override
    public Object filter(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        return "me1-"+fc.next(ctx);
    }
}

