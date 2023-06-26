package test.net.jplugin.core.service.incept;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.service.api.ServiceFactory;

public class ExtensionInceptTestClient {
    public void test(){
        AssertKit.assertEqual(ServiceFactory.getService(IService212.class).hello(),"me1-me3-me2-hello");
    }
}
