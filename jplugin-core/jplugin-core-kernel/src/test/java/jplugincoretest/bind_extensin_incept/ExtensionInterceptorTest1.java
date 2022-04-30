package jplugincoretest.bind_extensin_incept;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.AbstractExtensionInterceptor;
import net.jplugin.core.kernel.api.BindExtensionInterceptor;
import net.jplugin.core.kernel.api.ExtensionInterceptorContext;

@BindExtensionInterceptor( forExtensionPoints = "jplugincoretest.bind_extensin_incept.IMyInterface1097" ,methodNameFilter = "hello")
@BindExtensionInterceptor( forExtensions = "MyInterface1097Impl1" ,methodNameFilter = "*")
@BindExtensionInterceptor( forImplClasses = "jplugincoretest.bind_extensin_incept.MyInterface1097Impl1",methodNameFilter = "goodMorning")

public class ExtensionInterceptorTest1 extends AbstractExtensionInterceptor {

    @Override
    protected Object execute(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        Integer result = (Integer) fc.next(ctx);
        return result+1;
    }
}
