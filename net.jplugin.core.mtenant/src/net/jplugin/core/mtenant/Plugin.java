package net.jplugin.core.mtenant;

import net.jplugin.common.kits.http.filter.HttpFilterManager;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.mtenant.impl.AbstractSqlMultiTenantHanlder;
import net.jplugin.core.mtenant.impl.MtDataSourceWrapperService;

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
	 * mtenant.db.strategy=schema（默认）|merge
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
	public Plugin(){
		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable"))){
			ExtensionDasHelper.addConnWrapperExtension(this, MtDataSourceWrapperService.class);
//			ExtensionWebHelper.addServiceFilterExtension(this, MtInvocationFilter.class);
//			ExtensionWebHelper.addWebCtrlFilterExtension(this, MtInvocationFilter.class);
			PluginEnvirement.INSTANCE.getStartLogger().log("@@@ mtenant ENABLED! req-param="+ConfigFactory.getStringConfig("mtenant.req-param-name")+" dbfield="+ConfigFactory.getStringConfig("mtenant.field"));
		}else{
			PluginEnvirement.INSTANCE.getStartLogger().log("@@@ mtenant DISABLED!");
		}
	}
	
	@Override
	public void onCreateServices() {
		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable"))){
			HttpFilterManager.addFilter(new MTenantChain());
			AbstractSqlMultiTenantHanlder.initInstance();
		}
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.MULTI_TANANT;
	}

	@Override
	public void init() {
		
	}
	
}
