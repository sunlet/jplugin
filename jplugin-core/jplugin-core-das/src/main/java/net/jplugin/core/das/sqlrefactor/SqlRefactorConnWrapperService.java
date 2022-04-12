package net.jplugin.core.das.sqlrefactor;

import java.sql.Connection;

import net.jplugin.core.das.api.IConnectionWrapperService;

public class SqlRefactorConnWrapperService implements IConnectionWrapperService{

	@Override
	public Connection wrapper(String dataSourceName, Connection connection) {
		if (SqlRefactorManager.hasSqlRefactor())
			return new ConnectionWrapperForSqlRefactor(dataSourceName, connection);
		else return connection;
	}

}
