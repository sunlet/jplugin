package net.jplugin.core.das.dds.dsnamed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.dds.api.AbstractRouterDataSource;
import net.jplugin.core.das.dds.api.IRouterDataSource;
import net.jplugin.core.das.dds.api.RouterExecutionContext;
import net.jplugin.core.das.dds.impl.kits.SchemaCheckKit;
import net.jplugin.core.das.dds.select.TableNameAndCommandFinder;
import net.sf.jsqlparser.statement.Statement;

/**
 * 经过这个路由sql都是用datasourcename作schemaname的，把sql通过dsname路由到各个数据源。并把schemaname去除。
 * 只判断第一个table的shcemaname。
  *同时支持 INSERT SELECT语句。
 * @author LiuHang
 *
 */
public class DsNamedDataSource extends AbstractRouterDataSource {

	@Override
	public void init(String dataSoruceName, Map<String, String> config) {
	}

	@Override
	public Connection getTargetConnBefConnect() throws SQLException {
		return null;
	}

	@Override
	public PreparedStatement getTargetPreparedStmtBefCreate(IRouterDataSource routeDatasource, String sql)
			throws SQLException {
		Statement stmt = RouterExecutionContext.get().getStatement(sql);
		PreparedStatement result = InsertSelectKit.getTargetPreparedStmtBefCreate(stmt);
		if (result!=null) {
			return result;
		}else {
			String targetDs = getTargetDsAndRemoveSchema(stmt);
			
			//stmt此时已经修改
			PreparedStatement st = DataSourceFactory.getDataSource(targetDs).getConnection().prepareStatement(stmt.toString());
			return st;
		}
	}

	private String getTargetDsAndRemoveSchema(Statement stmt) throws SQLException {
		
		//计算schema
		Set<String> result = SchemaCheckKit.extractSchema(stmt,false);

		if (result.size()!=1) {
			throw new SQLException("You must have only one schma , but find:"+result.size()+"  sql="+stmt);
		}
		
		//去除schema
		SchemaCheckKit.extractAndRemoveSchema(stmt,false);
		
		String name = result.iterator().next();
		return name;
	}



	@Override
	public PreparedStatement getTargetPreparedStmtBefCreate(IRouterDataSource routeDatasource, String sql,
			int autoGenKeys) throws SQLException {
		throw new SQLException("NOT SUPPORT");
	}
	
	@Override
	public StatementResult getTargetStmtBefExecute(IRouterDataSource routeDatasource, String sql) throws SQLException {
		Statement stmt = RouterExecutionContext.get().getStatement(sql);
		StatementResult result = InsertSelectKit.getTargetStmtBefExecute(stmt);

		if (result!=null) {
			return result;
		}else {
			String targetDs = getTargetDsAndRemoveSchema(stmt);
			//stmt此时已经修改
			java.sql.Statement st = DataSourceFactory.getDataSource(targetDs).getConnection().createStatement();
			return StatementResult.with(st,stmt.toString());
		}
	}

	@Override
	public PreparedStatement getTargetPreparedStmtBefExecute(IRouterDataSource routeDatasource, String sql,
			List<Object> params) throws SQLException {
		throw new SQLException("NOT SUPPORT");
	}


}
