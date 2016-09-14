package net.jplugin.core.das.route.impl.conn;

import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.route.api.SqlHandleService;
import net.jplugin.core.das.route.api.TablesplitException;

public class NotPreparedStatementUtil {

	/**
	 * 这个返回用来执行带sql参数的情况
	 * @return
	 * @throws SQLException
	 */
	public static Result genTargetNotPreparedStatement(RouterConnection conn,String sql) throws SQLException {
		if (sql==null) throw new TablesplitException("No sql found");
		SqlHandleResult shr = SqlHandleService.INSTANCE.handle(conn,sql);
		
		DataSource tds = DataSourceFactory.getDataSource(shr.getTargetDataSourceName());
		if (tds==null) 
			throw new TablesplitException("Can't find target datasource."+shr.getTargetDataSourceName());
		Result result = new Result();
		result.statement = tds.getConnection().createStatement();
		result.resultSql = shr.resultSql;
		return result;
	}
	
	public static class Result{
		Statement statement;
		String resultSql;
	}
}
