package net.jplugin.core.das.dds.dsnamed;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.jplugin.core.das.dds.api.IRouterDataSource.StatementResult;
import net.jplugin.core.das.dds.kits.SqlParserKit;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;

public class InsertSelectKit {

	public static PreparedStatement getTargetPreparedStmtBefCreate(Statement stmt) {
		return null;
	}


	public static StatementResult getTargetStmtBefExecute(Statement stmt) throws SQLException {
		if (stmt instanceof Insert) {
			Insert insert = (Insert) stmt;
			if (insert.getSelect()!=null) {
				return InsertSelectStatement.create(stmt.toString());
			}
		}
		return null;
	}

}
