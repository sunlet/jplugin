package jplugincoretest.bind_extensin_incept;

import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.core.kernel.api.SetExtensionPriority;

@SetExtensionPriority(10)
@BindExtension
public class MyInterface1097Impl2 implements IMyInterface1097{
    @Override
    public int hello() {
        return 0;
    }

    @Override
    public int goodMorning() {
        return 0;
    }
}
