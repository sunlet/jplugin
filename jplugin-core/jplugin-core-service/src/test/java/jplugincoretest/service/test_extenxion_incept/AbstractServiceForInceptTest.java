package jplugincoretest.service.test_extenxion_incept;

import jplugincoretest.service.Plugin;
import net.jplugin.core.kernel.api.BindExtensionPoint;
import net.jplugin.core.kernel.api.PointType;

@BindExtensionPoint(type = PointType.LIST ,name = Plugin.EP_SERVICE_FOR_INCEPT)
public abstract class AbstractServiceForInceptTest {
    public static String staticVar;

    public String instanceVar;


    public abstract String hello(String name);
}
