package jplugincoretest.mybastic;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.PluginAnnotation;

@PluginAnnotation
public class Plugin extends AbstractPlugin {
    @Override
    public int getPrivority() {
        return CoreServicePriority.DAS_IBATIS+1;
    }

    @Override
    public void init() {

    }
}
