package net.jplugin.core.das.route;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.Extension;

public class ExtensionDasRouteHelper {
	
	public static void addRouteAlgmExtension(AbstractPlugin plugin,String algmName,Class algmClass){
		plugin.addExtension(Extension.create(net.jplugin.core.das.route.Plugin.EP_TS_ALGM, algmName,algmClass));
	}
	public static void addMulQryRsWrapperControllerExtension(AbstractPlugin plugin,Class wrapperCls){
		plugin.addExtension(Extension.create(net.jplugin.core.das.route.Plugin.EP_MULQRY_RS_WRAPCTRL,wrapperCls));
	}
	
	public static void addSqlFunctionExtension(AbstractPlugin plugin,String name,Class c){
		plugin.addExtension(Extension.create(net.jplugin.core.das.route.Plugin.EP_SQL_FUNCTION,name.toUpperCase(),c));
	}
	
	public static void addAggSqlFunctionExtension(AbstractPlugin plugin,String name,Class c){
		plugin.addExtension(Extension.create(net.jplugin.core.das.route.Plugin.EP_SQL_AGG_FUNCTION,name.toUpperCase(),c));
	}
}
