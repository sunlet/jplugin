package net.jplugin.core.das.api.sqlrefactor;

import java.sql.Connection;

public interface ISqlRefactor {
	public String refactSql(String dsName,String sql, Connection inner);
}
