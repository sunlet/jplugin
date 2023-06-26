package jplugincoretest.bind_extensin_incept;

import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.core.kernel.api.SetExtensionId;
import net.jplugin.core.kernel.api.SetExtensionPriority;

@SetExtensionId("MyInterface1097Impl1")
@SetExtensionPriority(20)
@BindExtension
public class MyInterface1097Impl1 implements IMyInterface1097{
    @Override
    public int hello() {
        return 0;
    }

    @Override
    public int goodMorning() {
        return 0;
    }
}
