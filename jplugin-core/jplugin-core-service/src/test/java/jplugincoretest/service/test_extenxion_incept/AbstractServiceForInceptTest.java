package jplugincoretest.service.test_extenxion_incept;

import jplugincoretest.service.Plugin;
import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.core.service.api.BindService;

@BindExtension(pointTo = Plugin.EP_SERVICE_FOR_INCEPT)
public class ServiceForInceptTest {
    public static String staticVar;

    public String instanceVar;

    public String hello(String name){
        return name;
    }
}
