package net.jplugin.ext.token;

import net.jplugin.core.ctx.ExtensionCtxHelper;
//import net.jplugin.core.das.hib.Plugin;
import net.jplugin.core.das.hib.api.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.ext.token.api.ITokenService;
import net.jplugin.ext.token.impl.DBToken;
import net.jplugin.ext.token.impl.TokenServiceImpl;

public class Plugin extends AbstractPlugin {
	public Plugin() {
		if (net.jplugin.core.das.hib.Plugin.noHib()) {
			System.out.println("Token plugin not start!");
			return;
		}else{
			System.out.println("Hib found,now start token.");
		}
		ExtensionDasHelper.addDataMappingExtension(this, DBToken.class);
		ExtensionCtxHelper.addRuleExtension(this,
				ITokenService.class.getName(), ITokenService.class,
				TokenServiceImpl.class);
	}

	@Override
	public void init() {
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.TOKEN;
	}

}
