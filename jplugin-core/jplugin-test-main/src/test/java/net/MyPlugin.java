package net;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.PluginAnnotation;

@PluginAnnotation
public class MyPlugin extends AbstractPlugin {
    @Override
    public int getPrivority() {
        return 0;
    }

    @Override
    public void init() {

    }
}
