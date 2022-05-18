package jplugincoretest.bind_extensin_incept;

import net.jplugin.core.kernel.api.BindExtensionPoint;
import net.jplugin.core.kernel.api.PointType;

@BindExtensionPoint(type = PointType.LIST, supportPriority = true)
public interface IMyInterface1097 {
    int hello();

    int goodMorning();

}
