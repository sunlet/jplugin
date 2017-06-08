package net.jplugin.core.das.monitor;

import java.sql.SQLException;

public interface SqlCall {
	public Object call() throws SQLException;
}
