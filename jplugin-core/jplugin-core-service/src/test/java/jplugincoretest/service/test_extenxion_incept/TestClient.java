package jplugincoretest.service.test_extenxion_incept;

import jplugincoretest.service.Plugin;
import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.BindBean;
import net.jplugin.core.kernel.api.Initializable;
import net.jplugin.core.kernel.api.RefExtensions;
import net.jplugin.core.service.api.RefService;

import java.util.List;

@BindBean(id="TestClient0423")
public class TestClient implements Initializable {

    @RefExtensions(pointTo = Plugin.EP_SERVICE_FOR_INCEPT)
    List<AbstractServiceForInceptTest> services;

    @Override
    public void initialize() {
        AssertKit.assertEqual("me-name",services.get(0).hello("name"));
    }
}
