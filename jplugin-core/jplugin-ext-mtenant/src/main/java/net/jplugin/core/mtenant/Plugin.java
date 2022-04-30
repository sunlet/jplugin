package net.jplugin.core.mtenant;

import net.jplugin.core.config.ExtensionConfigHelper;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionKernelHelper;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.mtenant.api.ITenantListProvidor;
import net.jplugin.core.mtenant.api.ITenantStoreStrategyProvidor;
import net.jplugin.core.mtenant.api.TenantListProvidorManager;
import net.jplugin.core.mtenant.dds.DDSConfigChangeHandler;
import net.jplugin.core.mtenant.handler.SqlMultiTenantHanlderSchemaImpl;
import net.jplugin.core.mtenant.handler2.SqlHandlerVisitorForMixed;
import net.jplugin.core.mtenant.handler2.SqlMultiTenantHanlderMixedImpl;
import net.jplugin.core.mtenant.handler2.TenantStoreStrategyManager;
import net.jplugin.core.mtenant.tidv.TenantIDValidator;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import net.jplugin.mtenant.impl.kit.SqlMultiTenantHanlderMergeImpl;

public class Plugin extends AbstractPlugin{
	
	/**
	 * <pre>
	 * #多租户配置列表
	 * #表示是否启用多租户模式，默认FALSE
	 * mtenant.enable=FALSE|TRUE
	 * 
	 * #HTTP请求中租户参数位置
	 * mtenant.req-param-at=BOTH|COOKIE|REQUEST
	 *  
	 * #HTTP请求中租户参数名称，参数中的名称、cookie中的名称都用这个配置，必须是相同的
	 * mtenant.req-param-name=xxxxx
	 * 

	 * 
	 * #启用多租户的数据源，默认ALL。可选配置。
	 * mtenant.datasource=ALL|ds1,ds2,ds3
	 * 
	 * #多租户数据库策略，这个配置目前不开放
	 * mtenant.db-strategy=schema（默认）|merge | mixed
	 *
	 * #分schema模式下各数据源对应的schemaprefix值，对于多租户的情况必须配置，否则会出错
	 * mtenant.schema-prefix.ds1=xxx
	 * mtenant.schema-prefix.ds2=yyy
	 * mtenant.schema-prefix.ds2=zzz
	 * 
	 * #并表方案还有一些其他配置，未在这里列出 ........
	 */
	
	/**
	 * 基于新的分schema方案，下面的配置不需要了！
	 * #指定区分多租户字段的名称.如果mtenant.enable=TRUE,则这项必须配置
	 * mtenant.field=field1
	 * 
	 * #指定各个数据源中不支持多租户的表（例外表）。可选配置
	 * mtenant.datasource.ds1.exclude=table1,table2
	 * mtenant.datasource.ds2.exclude=table1,table2
	 * 
	 * #说明一下
	 * #如果碰到忽略多租户处理的注释，则忽略该SQL的处理：\/* IGNORE-TANENT *\/
	 * </pre>
	 */
	public static final String EP_TENANTLIST_PROVIDOR = "EP_TENANTLIST_PROVIDOR";
	public static final String EP_TENANT_STORESTG_PROVIDOR = "EP_TENANT_STORESTG_PROVIDOR";
	public Plugin(){
		MtenantStatus.init();
		
		this.addExtensionPoint(ExtensionPoint.createList(EP_TENANTLIST_PROVIDOR, ITenantListProvidor.class));
		this.addExtensionPoint(ExtensionPoint.createList(EP_TENANT_STORESTG_PROVIDOR, ITenantStoreStrategyProvidor.class));
		
		ExtensionDasHelper.addDynamisDataSourceTypeExtension(this, "mt-dds", net.jplugin.core.mtenant.dds.MultitenantDynamicDataSource.class);

		if (MtenantStatus.enabled()){
			
//			ExtensionDasHelper.addConnWrapperExtension(this, MtDataSourceWrapperService.class);
			registeSqlRefectorExtension();
			
//			ExtensionWebHelper.addServiceFilterExtension(this, MtInvocationFilter.class);
//			ExtensionWebHelper.addWebCtrlFilterExtension(this, MtInvocationFilter.class);
			PluginEnvirement.INSTANCE.getStartLogger().log("@@@ mtenant ENABLED! req-param="+ConfigFactory.getStringConfigWithTrim("mtenant.req-param-name")+" dbfield="+ConfigFactory.getStringConfigWithTrim("mtenant.field"));
			ExtensionKernelHelper.addHttpClientFilterExtension(this, MTenantChain.class);
			//检查空值
			ExtensionWebHelper.addServiceFilterExtension(this, TenantIDValidator.class);
			ExtensionWebHelper.addWebCtrlFilterExtension(this, TenantIDValidator.class);
			
			ExtensionConfigHelper.addConfigChangeHandlerExtension(this, "*", DDSConfigChangeHandler.class);
		}else{
			PluginEnvirement.INSTANCE.getStartLogger().log("@@@ mtenant DISABLED!");
		}
		
		
	}
	
	private void registeSqlRefectorExtension() {
		String s = ConfigFactory.getStringConfigWithTrim("mtenant.db-strategy");
		if (s==null){
			s = "schema";
//			s = "merge";
		}
		Class clazz=null;
		if ("schema".equals(s)){
			clazz = SqlMultiTenantHanlderSchemaImpl.class;
//			instance = new SqlMultiTenantHanlderSchemaImpl();
		}else if ("merge".equals(s)){
			clazz= SqlMultiTenantHanlderMergeImpl.class;
//			instance = new SqlMultiTenantHanlderMergeImpl();
		}else if ("mixed".equals(s)){
			clazz= SqlMultiTenantHanlderMixedImpl.class;
		}else
			throw new RuntimeException("Error mtenant.db.strategy configed : "+s);
		
		ExtensionDasHelper.addSqlRefactorExtension(this, clazz);
		PluginEnvirement.getInstance().getStartLogger().log("多租户数据库策略:"+s+"  "+clazz.getName());

	}

	@Override
	public void onCreateServices() {
		if (MtenantStatus.enabled()){
//			HttpFilterManager.addFilter(new MTenantChain());
//			AbstractSqlMultiTenantHanlder.initInstance();
			//初始化配置
			TenantIDValidator.init();
			
			TenantListProvidorManager.instance.init();
			TenantStoreStrategyManager.instance.init();
			SqlHandlerVisitorForMixed.init();
		}
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.MULTI_TANANT;
	}

	@Override
	public void init() {
		
	}

	@Override
	public boolean searchClazzForExtension() {
		return false;
	}
	
}
