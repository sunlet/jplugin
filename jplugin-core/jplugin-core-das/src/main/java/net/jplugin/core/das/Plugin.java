package net.jplugin.core.das;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IConnectionWrapperService;
import net.jplugin.core.das.api.IDynamicDataSourceProvider;
import net.jplugin.core.das.api.dds.DDSManager;
import net.jplugin.core.das.api.impl.ConnectionWrapperManager;
import net.jplugin.core.das.api.impl.DataSourceDefinition;
import net.jplugin.core.das.api.impl.DynamicDataSourceManager;
import net.jplugin.core.das.api.monitor.ISqlExecFilter;
import net.jplugin.core.das.api.monitor.ISqlMonitorListener;
import net.jplugin.core.das.api.sqlrefactor.ISqlRefactor;
import net.jplugin.core.das.monitor.MonitorConnWrapperService;
import net.jplugin.core.das.monitor.SqlMonitor;
import net.jplugin.core.das.monitor.SqlMonitorListenerManager;
import net.jplugin.core.das.sqlrefactor.SqlRefactorConnWrapperService;
import net.jplugin.core.das.sqlrefactor.SqlRefactorManager;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;

public class Plugin extends AbstractPlugin {
	public static final String EP_DBSPLIT_ALG = "EP_DBSPLIT_ALG";
	public static final String EP_DATASOURCE = "EP_DATASOURCE";
	public static final String EP_UM_DATASOURCE = "EP_UM_DATASOURCE";
	public static final String EP_CONN_WRAPPER = "EP_CONN_WRAPPER";
	
	public static final String EP_SQL_LISTENER = "EP_SQL_LISTENER";
	public static final String EP_SQL_EXEC_FILTER = "EP_SQL_EXEC_FILTER";
	public static final String EP_SQL_REFACTOR = "EP_SQL_REFACTOR";
	public static final String EP_DYNAMIC_DS_PROVIDER = "EP_DYNAMIC_DS_PROVIDER";
	public static final String EP_DYNAMIC_DATASOURCE_TYPE="EP_DYNAMIC_DATASOURCE_TYPE";
	

	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.createNamed(EP_DATASOURCE, DataSourceDefinition.class));
		this.addExtensionPoint(ExtensionPoint.createList(EP_CONN_WRAPPER, IConnectionWrapperService.class));
		
		this.addExtensionPoint(ExtensionPoint.createList(EP_SQL_LISTENER, ISqlMonitorListener.class));
		this.addExtensionPoint(ExtensionPoint.createList(EP_SQL_EXEC_FILTER, ISqlExecFilter.class));
		this.addExtensionPoint(ExtensionPoint.createList(EP_SQL_REFACTOR, ISqlRefactor.class));
		this.addExtensionPoint(ExtensionPoint.createNamed(EP_DYNAMIC_DS_PROVIDER, IDynamicDataSourceProvider.class));
		
		this.addExtensionPoint(ExtensionPoint.createNamed(EP_DYNAMIC_DATASOURCE_TYPE,ClassDefine.class));


		
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
//		DynamicDataSourceProviderManager.INSTANCE.init();
		DDSManager.me.init();
		DynamicDataSourceManager.INSTANCE.init();
		DataSourceFactory.init();
		ConnectionWrapperManager.init();
		SqlMonitorListenerManager.instance.init();
		SqlMonitor.initExecFilter();
		SqlRefactorManager.init();
	}


	@Override
	public void init() {
		
	}


	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}

}
