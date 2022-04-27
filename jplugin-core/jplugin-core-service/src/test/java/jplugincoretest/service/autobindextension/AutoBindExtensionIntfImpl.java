package jplugincoretest.service.autobindextension;

import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.core.kernel.api.SetExtensionPriority;


@BindExtension
public class AutoBindExtensionIntfImpl implements  IAutoBindExtensionIntf{

    @Override
    public String say(String n) {
        return "hello"+n;
    }
}
