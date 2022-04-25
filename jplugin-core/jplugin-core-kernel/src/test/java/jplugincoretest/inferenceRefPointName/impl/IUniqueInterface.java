package jplugincoretest.inferenceRefPointName.impl;

import net.jplugin.core.kernel.api.MakeExtensionPoint;

@MakeExtensionPoint(type= MakeExtensionPoint.Type.UNIQUE)
public interface IMyInterface2 {
    void hi();
}
