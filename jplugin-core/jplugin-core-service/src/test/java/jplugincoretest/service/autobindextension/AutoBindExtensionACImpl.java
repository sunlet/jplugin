package jplugincoretest.service.autobindextension;

import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.core.kernel.api.SetExtensionPriority;

@SetExtensionPriority(113)
@BindExtension
public class AutoBindExtensionACImpl extends IAutoBindExtensionAbstractClass{
    @Override
    public void greet() {
        System.out.println("==============greet");

    }
}
