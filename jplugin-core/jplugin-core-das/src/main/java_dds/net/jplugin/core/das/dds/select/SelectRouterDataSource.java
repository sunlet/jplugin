package net.jplugin.core.das.dds.select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.dds.api.AbstractRouterDataSource;
import net.jplugin.core.das.dds.api.IRouterDataSource;
import net.jplugin.core.das.dds.api.RouterException;
import net.jplugin.core.das.dds.api.RouterExecutionContext;
import net.sf.jsqlparser.statement.Statement;
/**
 * 
 * @author LiuHang
 *
 */
public class SelectRouterDataSource extends AbstractRouterDataSource {
	SelectDatasourceConfig config;
	
	@Override
	public void init(String dsname,Map<String, String> config) {
		this.config = SelectDatasourceConfig.create(config);
	}

	@Override
	public Connection getTargetConnBefConnect() throws SQLException {
		return null;
	}

	@Override
	public PreparedStatement getTargetPreparedStmtBefCreate(IRouterDataSource routeDatasource, String sql)
			throws SQLException {
		String target = computeTargetDataSource(sql);
		return DataSourceFactory.getDataSource(target).getConnection().prepareStatement(sql);
	}
	
	@Override
	public PreparedStatement getTargetPreparedStmtBefCreate(IRouterDataSource routeDatasource, String sql,
			int autoGenKeys) throws SQLException {
		String target = computeTargetDataSource(sql);
		return DataSourceFactory.getDataSource(target).getConnection().prepareStatement(sql,autoGenKeys);
	}

	private String computeTargetDataSource(String sql) {
		Statement stmt = RouterExecutionContext.get().getStatement(sql);
		Tuple2<List<String>, String> result = TableNameAndCommandFinder.find(stmt);
		if (result.first.size()==0) {
			throw new RouterException("Can't find table name in sql:"+sql);
		}
		String target = this.config.getTargetDataSource(result.second, result.first.get(0));
		return target;
	}

	@Override
	public StatementResult getTargetStmtBefExecute(IRouterDataSource routeDatasource, String sql) throws SQLException {
		String target = computeTargetDataSource(sql);
		java.sql.Statement stmt = DataSourceFactory.getDataSource(target).getConnection().createStatement();
		return StatementResult.with(stmt, sql);
	}

	@Override
	public PreparedStatement getTargetPreparedStmtBefExecute(IRouterDataSource routeDatasource, String sql,
			List<Object> params) throws SQLException {
		throw new RouterException("shoudln't come here");
	}


}
