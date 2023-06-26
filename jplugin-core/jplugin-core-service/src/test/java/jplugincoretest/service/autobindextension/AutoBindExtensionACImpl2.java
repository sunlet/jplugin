package jplugincoretest.service.autobindextension;

import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.core.kernel.api.SetExtensionPriority;

@SetExtensionPriority(112)
@BindExtension
public class AutoBindExtensionACImpl2 extends IAutoBindExtensionAbstractClass {
    @Override
    public void greet() {
        System.out.println("good!===================");
    }
}
