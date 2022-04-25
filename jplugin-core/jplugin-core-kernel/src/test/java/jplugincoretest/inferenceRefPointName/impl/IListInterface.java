package jplugincoretest.inferenceRefPointName.impl;

import net.jplugin.core.kernel.api.MakeExtensionPoint;

@MakeExtensionPoint(type = MakeExtensionPoint.Type.LIST)
public interface IListInterface {
    public String greet();
}
