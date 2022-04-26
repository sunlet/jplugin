package jplugincoretest.service.test;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.PluginAnnotation;

@PluginAnnotation
public class Plugin extends AbstractPlugin {

    @Override
    public int getPrivority() {
        return 0;
    }

    @Override
    public void init() {

    }
}
