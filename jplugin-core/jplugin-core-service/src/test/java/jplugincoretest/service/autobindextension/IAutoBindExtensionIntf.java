package jplugincoretest.service.autobindextension;

import net.jplugin.core.kernel.api.BindExtensionPoint;
import net.jplugin.core.kernel.api.PointType;

@BindExtensionPoint( type = PointType.LIST)
public interface IAutoBindExtensionIntf {
    public String say(String n);
}
