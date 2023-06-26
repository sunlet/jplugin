package test.net.jplugin.core.service;

import net.jplugin.core.kernel.api.*;
import net.jplugin.core.service.ExtensionServiceHelper;
import test.net.jplugin.core.service.incept.*;

public class Plugin extends AbstractPluginForTest {
    public Plugin(){
        ExtensionServiceHelper.addServiceExtension(this, IService212.class, Service212Impl.class);
        Extension.setLastExtensionId("iservice212");

        ExtensionKernelHelper.addExtensionInterceptorExtension(this, ExtensionInceptTest1.class , "iservice212",null,null,null,null);
        Extension.setLastExtensionPriority((short) 10);
        ExtensionKernelHelper.addExtensionInterceptorExtension(this, ExtensionInceptTest2.class , "iservice212",null,null,null,null);
        Extension.setLastExtensionPriority((short) 30);
        ExtensionKernelHelper.addExtensionInterceptorExtension(this, ExtensionInceptTest3.class , "iservice212",null,null,null,null);
//        ExtensionKernelHelper.addExtensionInterceptorExtension(this, ExtensionInceptTest3.class , null, Constants.EP_SERVICE,null);
        Extension.setLastExtensionPriority((short) 20);

    }
    @Override
    public int getPrivority() {
        return CoreServicePriority.SERVICE+2;
    }

    @Override
    public void test() throws Throwable {
        new ExtensionInceptTestClient().test();
    }
}
