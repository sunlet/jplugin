package net.jplugin.core.mtanant.impl;

import java.sql.Connection;

import net.jplugin.core.das.api.IConnectionWrapperService;

public class MtDataSourceWrapperService implements IConnectionWrapperService {

	@Override
	public Connection wrapper(String dsname,Connection connection) {
		return new ConnectionWrapperForMt(dsname,connection);
	}
}
