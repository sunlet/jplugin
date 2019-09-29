package net.jplugin.core.das.route.impl.autocreate;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.route.impl.util.SqlParserKit;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class SelectStatementInspector {

	public void inspect(String sql){
		Statement stmt = SqlParserKit.parse(sql);
		AssertKit.assertTrue(stmt instanceof Select);
	}
	
	static class Visitor extends TablesNamesFinder{
		
	}
}
