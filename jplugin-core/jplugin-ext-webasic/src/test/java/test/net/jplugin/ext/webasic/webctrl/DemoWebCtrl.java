package test.net.jplugin.ext.webasic.webctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DemoWebCtrl {
    public void req(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String name = (String) req.getParameter("name");
        res.getWriter().write(name);
    }
}
