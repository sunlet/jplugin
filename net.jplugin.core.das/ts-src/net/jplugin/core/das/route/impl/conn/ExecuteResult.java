package net.jplugin.core.das.route.impl.conn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteResult {
	ResultSet rs;
	int updCount = -1;
	public void clear() {
		rs = null;
		updCount = -1;
	}
	public void fetch(Statement stmt) throws SQLException{
		rs = stmt.getResultSet();
		updCount = stmt.getUpdateCount();
	}
}