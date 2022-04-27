package jplugincoretest.inferenceRefPointName.impl;

import net.jplugin.core.kernel.api.DefineExtensionPoint;
import net.jplugin.core.kernel.api.PointType;

@DefineExtensionPoint(type= PointType.UNIQUE)
public interface IUniqueInterface {
    void hi();
}
