package net.jplugin.core.mtenant;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.mtenant.impl.MtDataSourceWrapperService;
import net.jplugin.core.mtenant.impl.filter.MtInvocationFilter;
import net.jplugin.ext.webasic.ExtensionWebHelper;

public class Plugin extends AbstractPlugin{
	/**
	 * <pre>
	 * #多租户配置列表
	 * #表示是否启用多租户模式，默认FALSE
	 * mtenant.enable=FALSE|TRUE
	 * 
	 * #HTTP请求中租户参数位置
	 *  mtenant.req-param-at=BOTH|SESSION|COOKIE
	 *  
	 * #HTTP请求中租户参数名称，参数中的名称、cookie中的名称都用这个配置，必须是相同的
	 * mtenant.req-param-name=xxxxx
	 * 
	 * 
	 * #指定区分多租户字段的名称.如果mtenant.enable=TRUE,则这项必须配置
	 * mtenant.field=field1
	 * 
	 * #启用多租户的数据源，默认ALL。可选配置。
	 * mtenant.datasource=ALL|ds1,ds2,ds3
	 * 
	 * #指定各个数据源中不支持多租户的表（例外表）。可选配置
	 * mtenant.datasource.ds1.exclude=table1,table2
	 * mtenant.datasource.ds2.exclude=table1,table2
	 * 
	 * #说明一下
	 * #如果碰到忽略多租户处理的注释，则忽略该SQL的处理：\/* IGNORE-TANANT *\/
	 * </pre>
	 */
	public Plugin(){
		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable"))){
			ExtensionDasHelper.addConnWrapperExtension(this, MtDataSourceWrapperService.class);
			ExtensionWebHelper.addServiceFilterExtension(this, MtInvocationFilter.class);
			ExtensionWebHelper.addWebCtrlFilterExtension(this, MtInvocationFilter.class);
		}else{
			System.out.println("@@@ mtenant ignore!");
		}
	}
	
	@Override
	public void init() {
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.MULTI_TANANT;
	}
	
}
