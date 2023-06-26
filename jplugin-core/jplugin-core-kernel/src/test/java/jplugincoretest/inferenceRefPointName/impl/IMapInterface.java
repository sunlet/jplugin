package jplugincoretest.inferenceRefPointName.impl;

import net.jplugin.core.kernel.api.BindExtensionPoint;
import net.jplugin.core.kernel.api.PointType;

@BindExtensionPoint(type = PointType.NAMED)
public interface IMapInterface {
    void greet(String s);
}
