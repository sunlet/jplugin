package jplugincoretest.inferenceRefPointName.impl;

import net.jplugin.core.kernel.api.MakeExtensionPoint;

@MakeExtensionPoint(type = MakeExtensionPoint.Type.NAMED)
public interface IMapInterface {
    void greet(String s);
}
