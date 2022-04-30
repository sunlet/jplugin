package jplugincoretest.service.test_extenxion_incept;

import jplugincoretest.service.Plugin;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.BindExtensionInterceptor;
import net.jplugin.core.kernel.api.ExtensionInterceptorContext;
import net.jplugin.core.kernel.api.AbstractExtensionInterceptor;

@BindExtensionInterceptor(forExtensionPoints = Plugin.EP_SERVICE_FOR_INCEPT)
public class ExtensionInterceptorTest extends AbstractExtensionInterceptor {
    @Override
    public Object filter(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        if (ctx.getMethod().getName().equals("hello"))
            return "me-"+fc.next(ctx);
        else
            return fc.next(ctx);
    }
}
