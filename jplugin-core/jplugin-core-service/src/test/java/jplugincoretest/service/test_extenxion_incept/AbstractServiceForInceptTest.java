package jplugincoretest.service.test_extenxion_incept;

import jplugincoretest.service.Plugin;
import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.core.kernel.api.MakeExtensionPoint;
import net.jplugin.core.kernel.api.RefBean;

@MakeExtensionPoint(type = MakeExtensionPoint.Type.LIST ,name = Plugin.EP_SERVICE_FOR_INCEPT)
public abstract class AbstractServiceForInceptTest {
    public static String staticVar;

    public String instanceVar;


    public abstract String hello(String name);
}
