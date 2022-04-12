package net.jplugin.core.das.dds.kits;

import java.io.StringReader;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;

public abstract class SqlParserKit {

	public static Statement parse(String sql) {
		try {
			CCJSqlParserManager pm = new CCJSqlParserManager();
			Statement statement = pm.parse(new StringReader(sql));
			return statement;
		} catch (Exception e) {
			throw new RuntimeException("sql parse error:"+sql);
		}
	}
}
