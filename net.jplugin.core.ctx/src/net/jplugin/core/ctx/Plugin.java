package net.jplugin.core.ctx;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jplugin.core.ctx.api.RuleServiceDefinition;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.ctx.api.TransactionHandler;
import net.jplugin.core.ctx.api.IRuleServiceFilter;
import net.jplugin.core.ctx.api.ITransactionManagerListener;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.ctx.impl.DefaultRuleInvocationHandler;
import net.jplugin.core.ctx.impl.RuleInvocationContext;
import net.jplugin.core.ctx.impl.RuleServiceAttrAnnoHandler;
import net.jplugin.core.ctx.impl.TransactionManagerAdaptor;
import net.jplugin.core.ctx.impl.TxMgrListenerManager;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionKernelHelper;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.service.api.Constants;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 上午09:02:06
 **/

public class Plugin extends AbstractPlugin{
	public static final String EP_RULE_SERVICE="EP_RULE_SERVICE";
	
	/*
	 * TX 定义为一个扩展点，不让别人随别可以获取到，因为我们想增加一个adaptor，用户只能通过TransactionServiceFactory来获取
	 */
	public static final String EP_TXMGR_LISTENER="EP_TXMGR_LISTENER";

	public static final String EP_RULE_SERVICE_FILTER = "EP_RULE_SERVICE_FILTER";
	
	public Plugin(){

		addExtensionPoint(ExtensionPoint.create(EP_RULE_SERVICE, RuleServiceDefinition.class,true));
		addExtensionPoint(ExtensionPoint.create(EP_TXMGR_LISTENER, ITransactionManagerListener.class,false));
		addExtensionPoint(ExtensionPoint.create(EP_RULE_SERVICE_FILTER, IRuleServiceFilter.class,false));
		
		addExtension(Extension.create(Constants.EP_SERVICE, RuleServiceFactory.class.getName(),RuleServiceFactory.class));
		addExtension(Extension.create(Constants.EP_SERVICE, TransactionManager.class.getName(),TransactionManagerAdaptor.class));
		
		ExtensionKernelHelper.addAnnoAttrHandlerExtension(this, RuleServiceAttrAnnoHandler.class);
	}
	/* (non-Javadoc)
	 * @see net.luis.common.kernel.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.CTX;
	}


	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void onCreateServices() {
		TransactionManagerAdaptor.init();
		RuleInvocationContext.init();
		
//		TransactionManager[] txms = PluginEnvirement.getInstance().getExtensionObjects(EP_TX_SERVICE_IMPLEMENTATION,TransactionManager.class);
//		if (txms.length==0){
//			txmf.init(null);
//		}else if (txms.length==1){
//			txmf.init(txms[0]);
//		}else{
//			throw new PluginRuntimeException("txm has more then 1 impletation");
//		}
		
		RuleServiceFactory ruleSvcFactory = ServiceFactory.getService(RuleServiceFactory.class);
//		RuleServiceDefinition[] defs = PluginEnvirement.getInstance().getExtensionObjects(EP_RULE_SERVICE, RuleServiceDefinition.class);
		Map<String,RuleServiceDefinition> defs = PluginEnvirement.getInstance().getExtensionMap(EP_RULE_SERVICE,RuleServiceDefinition.class);
		ruleSvcFactory.init(defs);
		
		TxMgrListenerManager.init();
		
		DefaultRuleInvocationHandler.init();
		
	}
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
