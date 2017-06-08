package net.jplugin.core.das.monitor;

import java.sql.Connection;

import net.jplugin.core.das.api.IConnectionWrapperService;

public class MonitorConnWrapperService implements IConnectionWrapperService{
	
	@Override
	public Connection wrapper(String dataSourceName, Connection connection) {
		if (SqlMonitorListenerManager.instance.hasListener())
			return new ConnectionWrapper(connection,dataSourceName);
		else 
			return connection;
	}

}
