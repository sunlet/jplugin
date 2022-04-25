package jplugincoretest.inferenceRefPointName.impl;

import net.jplugin.core.kernel.api.MakeExtensionPoint;

@MakeExtensionPoint(type= MakeExtensionPoint.Type.UNIQUE)
public interface IUniqueInterface {
    void hi();
}
