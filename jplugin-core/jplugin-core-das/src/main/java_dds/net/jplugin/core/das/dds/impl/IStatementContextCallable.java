package net.jplugin.core.das.dds.impl;

import java.sql.SQLException;

public interface IStatementContextCallable {
	public Object call() throws SQLException;
}
