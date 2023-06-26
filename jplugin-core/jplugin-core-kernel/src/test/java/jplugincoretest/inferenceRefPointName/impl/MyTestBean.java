package jplugincoretest.inferenceRefPointName.impl;

import net.jplugin.core.kernel.api.*;

import java.util.List;
import java.util.Map;

@BindBean(id="bean278345")
public class MyTestBean implements Initializable {

//    @RefExtensions(pointTo = "jplugincoretest.inferenceRefPointName.impl.IMyInterface")
    @RefExtensions()
    List<IListInterface> intfs;
    @Override
    public void initialize() {
        System.out.println(intfs.get(0).greet());

        intfMap.get("abc").greet("aaa");

        unique.hi();
    }

    @RefExtension()
    IUniqueInterface unique;

    @RefExtensionMap()
    Map<String,IMapInterface> intfMap;

}
