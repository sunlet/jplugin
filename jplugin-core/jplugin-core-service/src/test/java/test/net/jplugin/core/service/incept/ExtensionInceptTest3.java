package test.net.jplugin.core.kernel.incept;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.ExtensionInterceptorContext;
import net.jplugin.core.kernel.api.IExtensionInterceptor;

public class ExtensionInceptTest3 implements IExtensionInterceptor {
    @Override
    public Object filter(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        return "me3-"+fc.next(ctx);
    }
}
