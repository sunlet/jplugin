package jplugincoretest.bind_extension_incept2;

import net.jplugin.core.kernel.api.BindExtensionPoint;
import net.jplugin.core.kernel.api.PointType;

@BindExtensionPoint(type = PointType.LIST, supportPriority = true)
public abstract class AbsMyInterface1097 {
    public abstract  int hello();

    public abstract  int goodMorning();

}
