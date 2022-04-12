package net.jplugin.core.das.api;

import java.sql.Connection;

public interface IConnectionWrapperService {
	public Connection wrapper(String dataSourceName, Connection connection);
}
