package jplugincoretest.service.test_extenxion_incept;

import jplugincoretest.service.Plugin;
import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.*;


@BindExtension(pointTo = Plugin.EP_SERVICE_FOR_INCEPT)
public class ServiceForInceptTest extends AbstractServiceForInceptTest{

    @RefBean(id="Bean111111")
    Bean1 bbb;

    @Override
    public String hello(String name) {
        System.out.println(this.toString());
        System.out.println(this.bbb.a());
        return name;
    }

//    public String toString(){
//        return "aaaaa";
//    }

//    @SetExtensionId("aaaa")
    @BindBean(id="Bean111111")
    public static class Bean1 {
        String ffff = "aa";

        public String a(){
            return ffff;

        }

        public String toString(){
            return ffff;
        }
    }
}
