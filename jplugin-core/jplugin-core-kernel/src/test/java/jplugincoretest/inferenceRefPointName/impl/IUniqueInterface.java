package jplugincoretest.inferenceRefPointName.impl;

import net.jplugin.core.kernel.api.BindExtensionPoint;
import net.jplugin.core.kernel.api.PointType;

@BindExtensionPoint(type= PointType.UNIQUE)
public interface IUniqueInterface {
    void hi();
}
