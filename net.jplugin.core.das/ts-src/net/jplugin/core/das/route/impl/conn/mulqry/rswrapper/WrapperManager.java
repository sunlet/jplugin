package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import java.sql.ResultSet;

import net.jplugin.core.kernel.api.PluginEnvirement;

public class WrapperManager {
	public static WrapperManager INSTANCE = new WrapperManager();
	private WrapperController[] controllers;
	public void init(){
		this.controllers = PluginEnvirement.getInstance().getExtensionObjects(net.jplugin.core.das.route.Plugin.EP_MULQRY_RS_WRAPCTRL, WrapperController.class);
	}
	
	public ResultSet wrap(ResultSet rs){
		ResultSet temp = rs;
		for (WrapperController c:controllers){
			if (c.needWrap()){
				temp = c.wrap(temp);
			}
		}
		return temp;
	}
}