package net.jplugin.core.kernel;

import net.jplugin.common.kits.http.filter.IHttpClientFilter;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.kernel.api.IExeRunnableInitFilter;
import net.jplugin.core.kernel.api.IExecutorFilter;
import net.jplugin.core.kernel.api.IPluginEnvInitFilter;
import net.jplugin.core.kernel.api.IScheduledExecutionFilter;
import net.jplugin.core.kernel.api.IStartup;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.impl.HttpClientFilterManager;
import net.jplugin.core.kernel.kits.ExecutorKitFilterManager;
import net.jplugin.core.kernel.kits.RunnableInitFilterManager;
import net.jplugin.core.kernel.kits.scheduled.ScheduledFilterManager;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 下午01:07:22
 **/

public class Plugin extends AbstractPlugin{

	public static final String EP_STARTUP = "EP_STARTUP";
	public static final String EP_ANNO_FOR_ATTR = "EP_ANNO_FOR_ATTR";
	public static final String EP_EXECUTOR_FILTER = "EP_EXECUTOR_FILTER";
	public static final String EP_HTTP_CLIENT_FILTER = "EP_HTTP_CLIENT_FILTER";
	public static final String EP_EXE_RUN_INIT_FILTER = "EP_EXE_RUN_INIT_FILTER";
	public static final String EP_PLUGIN_ENV_INIT_FILTER = "EP_PLUGIN_ENV_INIT_FILTER";
	public static final String EP_EXE_SCHEDULED_FILTER = "EP_EXE_SCHEDULED_FILTER";

	public Plugin(){
		addExtensionPoint(ExtensionPoint.create(EP_STARTUP, IStartup.class));
		addExtensionPoint(ExtensionPoint.create(EP_ANNO_FOR_ATTR, IAnnoForAttrHandler.class));
		addExtensionPoint(ExtensionPoint.create(EP_EXECUTOR_FILTER,IExecutorFilter.class));
		addExtensionPoint(ExtensionPoint.create(EP_EXE_RUN_INIT_FILTER,IExeRunnableInitFilter.class));
		addExtensionPoint(ExtensionPoint.create(EP_HTTP_CLIENT_FILTER,IHttpClientFilter.class));	
		addExtensionPoint(ExtensionPoint.create(EP_PLUGIN_ENV_INIT_FILTER,IPluginEnvInitFilter.class));	
		addExtensionPoint(ExtensionPoint.create(EP_EXE_SCHEDULED_FILTER,IScheduledExecutionFilter.class));	
	}
	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.KERNEL;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void onCreateServices() {
		HttpClientFilterManager.init();
		ExecutorKitFilterManager.init();
		RunnableInitFilterManager.init();
		PluginEnvirement.getInstance().initStartFilter();
		ScheduledFilterManager.init();
	}
	
	@Override
	public void init() {
		
	}

}
