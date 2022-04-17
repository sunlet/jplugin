package test.net.jplugin.ext.webasic.webctrl;

import net.jplugin.ext.webasic.api.AbstractExController;
import net.jplugin.ext.webasic.impl.web.webex.WebExController;

import java.io.IOException;

public class DemoWebExCtrl extends AbstractExController {

    public void test() throws IOException {
        String name = (String) getAttr("name");
        getRes().getWriter().write(name);
    }

}
