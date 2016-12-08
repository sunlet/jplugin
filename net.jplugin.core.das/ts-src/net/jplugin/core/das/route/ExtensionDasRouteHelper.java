package net.jplugin.core.das.route;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;

public class ExtensionDasRouteHelper {
	
	public static void addRouteAlgmExtension(AbstractPlugin plugin,String algmName,Class algmClass){
		plugin.addExtension(Extension.create(net.jplugin.core.das.route.Plugin.EP_TS_ALGM, algmName,algmClass));
	}


}
