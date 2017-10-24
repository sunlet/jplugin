package net.jplugin.core.das;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IConnectionWrapperService;
import net.jplugin.core.das.api.impl.ConnectionWrapperManager;
import net.jplugin.core.das.api.impl.DataSourceDefinition;
import net.jplugin.core.das.api.monitor.ISqlExecFilter;
import net.jplugin.core.das.api.monitor.ISqlMonitorListener;
import net.jplugin.core.das.api.sqlrefactor.ISqlRefactor;
import net.jplugin.core.das.monitor.MonitorConnWrapperService;
import net.jplugin.core.das.monitor.SqlMonitor;
import net.jplugin.core.das.monitor.SqlMonitorListenerManager;
import net.jplugin.core.das.sqlrefactor.SqlRefactorConnWrapperService;
import net.jplugin.core.das.sqlrefactor.SqlRefactorManager;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;

public class Plugin extends AbstractPlugin {
	public static final String EP_DBSPLIT_ALG = "EP_DBSPLIT_ALG";
	public static final String EP_DATASOURCE = "EP_DATASOURCE";
	public static final String EP_UM_DATASOURCE = "EP_UM_DATASOURCE";
	public static final String EP_CONN_WRAPPER = "EP_CONN_WRAPPER";
	
	public static final String EP_SQL_LISTENER = "EP_SQL_LISTENER";
	public static final String EP_SQL_EXEC_FILTER = "EP_SQL_EXEC_FILTER";
	public static final String EP_SQL_REFACTOR = "EP_SQL_REFACTOR";
	

	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.create(EP_DATASOURCE, DataSourceDefinition.class,true));
		this.addExtensionPoint(ExtensionPoint.create(EP_CONN_WRAPPER, IConnectionWrapperService.class));
		
		this.addExtensionPoint(ExtensionPoint.create(EP_SQL_LISTENER, ISqlMonitorListener.class));
		this.addExtensionPoint(ExtensionPoint.create(EP_SQL_EXEC_FILTER, ISqlExecFilter.class));
		this.addExtensionPoint(ExtensionPoint.create(EP_SQL_REFACTOR, ISqlRefactor.class));
		
		//用来监控sql的ConnectionWrapper，先注册的在里面一层，所以能看到外面包装造成的影响，应该能监控到Refactor
		//以后的sql
		ExtensionDasHelper.addConnWrapperExtension(this, MonitorConnWrapperService.class);
		
		//用来实现SqlRefactor的ConnectionWrapper
		ExtensionDasHelper.addConnWrapperExtension(this, SqlRefactorConnWrapperService.class);
	}


	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS;
	}
	
	public void onCreateServices() {
		DataSourceFactory.init();
		ConnectionWrapperManager.init();
		SqlMonitorListenerManager.instance.init();
		SqlMonitor.initExecFilter();
		SqlRefactorManager.init();
	}


	@Override
	public void init() {
		
	}

}
