package jplugincoretest.service.autobindextension;

import net.jplugin.core.kernel.api.Initializable;
import net.jplugin.core.kernel.api.RefExtension;
import net.jplugin.core.kernel.api.RefExtensions;
import net.jplugin.core.kernel.api.SetExtensionPriority;
import net.jplugin.core.service.api.BindService;

import java.util.List;


@BindService(accessClass = AutoBindTest.class)
public class AutoBindTest implements Initializable {

    @RefExtension(pointTo = "jplugincoretest.service.autobindextension.IAutoBindExtensionAbstractClass")
    IAutoBindExtensionAbstractClass s1;

    @RefExtensions
    List<IAutoBindExtensionIntf> s2;


    @Override
    public void initialize() {
        s1.greet();
        s2.get(0).say("hello");
    }
}
