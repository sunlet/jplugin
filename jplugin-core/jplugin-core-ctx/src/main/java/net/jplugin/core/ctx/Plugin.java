package net.jplugin.core.ctx;

import java.util.Map;

import net.jplugin.core.ctx.api.*;
import net.jplugin.core.ctx.impl.DefaultRuleInvocationHandler;
import net.jplugin.core.ctx.impl.RuleInvocationContext;
import net.jplugin.core.ctx.impl.RuleServiceAttrAnnoHandler;
import net.jplugin.core.ctx.impl.ServiceExtensionResolver;
import net.jplugin.core.ctx.impl.TransactionManagerAdaptor;
import net.jplugin.core.ctx.impl.TxMgrListenerManager;
import net.jplugin.core.ctx.impl.filter4clazz.RuleCallFilterDefineManager;
import net.jplugin.core.ctx.impl.filter4clazz.RuleCallFilterDefineBean;
import net.jplugin.core.ctx.impl.filter4clazz.RuleCallFilterManagerRuleFilter;
import net.jplugin.core.ctx.impl.usetxincept.UseTransactionIncept;
import net.jplugin.core.ctx.ruleincept.ExtensionInterceptor4Rule;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.AutoBindExtensionManager;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionKernelHelper;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.service.api.Constants;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.core.service.api.UseTransaction;
import net.jplugin.core.service.impl.ServiceAttrAnnoHandler;
import net.jplugin.core.service.impl.esf.ESFHelper2;

import static net.jplugin.core.service.Plugin.EP_SERVICE;

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
	
	public static final String EP_RULE_METHOD_INTERCEPTOR = "EP_RULE_METHOD_INTERCEPTOR";
	
	static{
		AutoBindExtensionManager.INSTANCE.addBindExtensionHandler((p)->{
			ExtensionCtxHelper.autoBindRuleMethodInterceptor(p, "");
			ExtensionCtxHelper.autoBindRuleServiceExtension(p, "");
		});
	}
	
	public Plugin(){

//		addExtensionPoint(ExtensionPoint.create(EP_RULE_SERVICE, RuleServiceDefinition.class,true));
		addExtensionPoint(ExtensionPoint.createNamed(EP_RULE_SERVICE, Object.class));
		addExtensionPoint(ExtensionPoint.createList(EP_TXMGR_LISTENER, ITransactionManagerListener.class));
		addExtensionPoint(ExtensionPoint.createList(EP_RULE_SERVICE_FILTER, IRuleServiceFilter.class));
		addExtensionPoint(ExtensionPoint.createList(EP_RULE_METHOD_INTERCEPTOR, RuleCallFilterDefineBean.class));
		
		
		addExtension(Extension.create(Constants.EP_SERVICE, RuleServiceFactory.class.getName(),RuleServiceFactory.class));
		addExtension(Extension.create(Constants.EP_SERVICE, TransactionManager.class.getName(),TransactionManagerAdaptor.class));
		
		ExtensionKernelHelper.addAnnoAttrHandlerExtension(this, RuleServiceAttrAnnoHandler.class);
		
		ExtensionCtxHelper.addRuleServiceFilterExtension(this,RuleCallFilterManagerRuleFilter.class );

		//ExtensionInterceptor方式支持Rule
		ExtensionKernelHelper.addExtensionInterceptorExtension(this, ExtensionInterceptor4Rule.class,null,EP_RULE_SERVICE,null,"*" , null);
		//因为这个执行的时候会把后面的filter忽略掉，所以要把优先级设为最大值
		Extension.setLastExtensionPriority(Integer.MAX_VALUE);

		//Service支持UseTransaction方法标注
		ExtensionKernelHelper.addExtensionInterceptorExtension(this, UseTransactionIncept.class,null,EP_SERVICE,null,null , UseTransaction.class);

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
		
		//初始化分类的Rule方法拦截过滤器
		RuleCallFilterDefineManager.INSTANCE.initialize();
		
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
//		Map<String,RuleServiceDefinition> defs = PluginEnvirement.getInstance().getExtensionMap(EP_RULE_SERVICE,RuleServiceDefinition.class);
//		ruleSvcFactory.init(defs);
		ruleSvcFactory.init();
		
		TxMgrListenerManager.init();
		
		DefaultRuleInvocationHandler.init();
	
		//注册一下这个扩展的resolver
//		ServiceAttrAnnoHandler.serviceExtensionResolver = new ServiceExtensionResolver();
	}
	public void init() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}

}
