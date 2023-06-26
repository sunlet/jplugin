package jplugincoretest.net.jplugin.ext.webasic.webctrl;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.HttpStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebCtrlTestClient {
    public void test() throws HttpStatusException, IOException {
        Map<String, Object> p = new HashMap<>();
        p.put("name","nnn");

        String result = HttpKit.post("http://localhost:8080/demo/demowebctrl/req.do", p);
        AssertKit.assertEqual("nnn",result);
    }
}
