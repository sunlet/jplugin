package jplugincoretest.service.autobindextension;

import net.jplugin.core.kernel.api.DefineExtensionPoint;
import net.jplugin.core.kernel.api.PointType;

@DefineExtensionPoint( type = PointType.LIST)
public interface IAutoBindExtensionIntf {
    public String say(String n);
}
