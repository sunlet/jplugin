package net.jplugin.core.mtenant.dds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.dds.api.AbstractRouterDataSource;
import net.jplugin.core.das.dds.api.IRouterDataSource;

public class MultitenantDynamicDataSource extends AbstractRouterDataSource{

	private static final String SHOULDN_T_COME_HERE = "shouldn't come here";

	MultiTenantDDSHelper helper ;
	@Override
	public void init(String dataSoruceName, Map<String, String> config) {
		helper = new MultiTenantDDSHelper();
		helper.init(dataSoruceName,config);
	}

	@Override
	public Connection getTargetConnBefConnect() throws SQLException {
		String ds = helper.computeTargetDataSource();
		return DataSourceFactory.getDataSource(ds).getConnection();
	}

	@Override
	public PreparedStatement getTargetPreparedStmtBefCreate(IRouterDataSource routeDatasource, String sql)
			throws SQLException {
		throw new RuntimeException(SHOULDN_T_COME_HERE);
	}

	@Override
	public PreparedStatement getTargetPreparedStmtBefExecute(IRouterDataSource routeDatasource, String sql,
			List<Object> params) throws SQLException {
		throw new RuntimeException(SHOULDN_T_COME_HERE);
	}

	@Override
	public StatementResult getTargetStmtBefExecute(IRouterDataSource routeDatasource, String sql) throws SQLException {
		throw new RuntimeException(SHOULDN_T_COME_HERE);
	}

	@Override
	public PreparedStatement getTargetPreparedStmtBefCreate(IRouterDataSource routeDatasource, String sql,
			int autoGenKeys) throws SQLException {
		throw new RuntimeException(SHOULDN_T_COME_HERE);
	}

}
