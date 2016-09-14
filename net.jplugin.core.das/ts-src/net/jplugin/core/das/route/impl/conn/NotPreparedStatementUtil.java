package net.jplugin.core.das.route.impl.conn;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	public static PreparedStatement genTargetNotPreparedStatement(RouterConnection conn,String sql) throws SQLException {
		if (sql==null) throw new TablesplitException("No sql found");
		SqlHandleResult shr = SqlHandleService.INSTANCE.handle(conn,sql);
		
		DataSource tds = DataSourceFactory.getDataSource(shr.getTargetDataSourceName());
		if (tds==null) 
			throw new TablesplitException("Can't find target datasource."+shr.getTargetDataSourceName());
		PreparedStatement stmt = tds.getConnection().prepareStatement("");//这种得到preparedstatement方法不知道正确否
		return stmt;
	}
}
