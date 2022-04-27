package jplugincoretest.service.autobindextension;

import net.jplugin.core.kernel.api.DefineExtensionPoint;
import net.jplugin.core.kernel.api.PointType;
import net.jplugin.core.kernel.api.SetExtensionPriority;


@DefineExtensionPoint( type = PointType.UNIQUE,supportPriority = true)
public abstract class IAutoBindExtensionAbstractClass {

    public abstract  void greet();
}
