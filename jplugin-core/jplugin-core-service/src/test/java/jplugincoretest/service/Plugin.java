package jplugincoretest.service;

import net.jplugin.core.kernel.api.*;
import net.jplugin.core.service.api.Constants;

@PluginAnnotation
public class Plugin extends AbstractPlugin {

    public static final String EP_SERVICE_FOR_INCEPT = "epServiceForIncept001";

    public Plugin(){
//        this.addExtensionPoint(ExtensionPoint.createList(EP_SERVICE_FOR_INCEPT,Object.class));
    }
    @Override
    public int getPrivority() {
        return CoreServicePriority.SERVICE +1;
    }

    @Override
    public void init() {

    }
}
