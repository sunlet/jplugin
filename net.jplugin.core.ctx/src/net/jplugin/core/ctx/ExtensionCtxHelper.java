package net.jplugin.core.ctx;

import net.jplugin.core.ctx.api.RuleServiceDefinition;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 上午10:22:41
 **/

public class ExtensionCtxHelper {
	public static void addRuleExtension(AbstractPlugin plugin,String name,Class intf,Class impl){
		plugin.addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_RULE_SERVICE, name,RuleServiceDefinition.class,new String[][]{{"interf",intf.getName()},{"impl",impl.getName()}} ));
	}
	
	public static void addRuleExtension(AbstractPlugin plugin,Class intf,Class impl){
		plugin.addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_RULE_SERVICE, intf.getName(),RuleServiceDefinition.class,new String[][]{{"interf",intf.getName()},{"impl",impl.getName()}} ));
	}
	
	public static void addTxMgrListenerExtension(AbstractPlugin plugin,Class impl){
		plugin.addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_TXMGR_LISTENER, impl ));
	}

}
