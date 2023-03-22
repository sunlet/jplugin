package net.luis.main.export;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.service.api.RefService;

@PluginAnnotation
public class Plugin extends AbstractPlugin {
    public Plugin(){

    }
    @Override
    public int getPrivority() {
        return 0;
    }

    @RefService
    IMyExport iexp;
    @RefService
    MyExport  exp;
    @Override
    public void init() {
        AssertKit.assertNotNull(iexp,"iexp");
        AssertKit.assertNotNull(exp,"exp");

    }
}
