package jplugincoretest.inferenceRefPointName.impl;

import net.jplugin.core.kernel.api.BindExtension;

@BindExtension()
public class ListInterfaceImpl1 implements IListInterface {
    @Override
    public String greet() {
        return "good morning";
    }
}
