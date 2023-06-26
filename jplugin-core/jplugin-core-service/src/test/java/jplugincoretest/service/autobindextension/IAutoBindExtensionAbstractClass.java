package jplugincoretest.service.autobindextension;

import net.jplugin.core.kernel.api.BindExtensionPoint;
import net.jplugin.core.kernel.api.PointType;


@BindExtensionPoint( type = PointType.UNIQUE,supportPriority = true)
public abstract class IAutoBindExtensionAbstractClass {

    public abstract  void greet();
}
