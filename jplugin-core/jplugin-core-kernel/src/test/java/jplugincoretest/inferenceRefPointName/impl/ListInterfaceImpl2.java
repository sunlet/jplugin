package jplugincoretest.inferenceRefPointName.impl;


import net.jplugin.core.kernel.api.BindExtension;

@BindExtension
public class MyInterfaceImpl2 extends MyInterfaceImpl1 implements IListInterface {

    @Override
    public String greet() {
        return "aaa";
    }
//
//    @Override
//    public void hi() {
//
//    }
}
