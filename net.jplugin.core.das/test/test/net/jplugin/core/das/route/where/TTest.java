package test.net.jplugin.core.das.route.where;

import java.io.StringReader;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

public class TTest {

	public static void main(String[] args) throws JSQLParserException {
		String sql = "select * from tb where f1=2 limit 5";
		Select r = parse(sql);
		System.out.println(r.toString());
	}
	
	private static Select parse(String sql) throws JSQLParserException {
		CCJSqlParserManager pm = new CCJSqlParserManager();
		Statement stmt = pm.parse(new StringReader(sql));
		return (Select)stmt;
	}
}
