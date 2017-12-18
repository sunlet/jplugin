package test.net.jplugin.core.das.route.where;

import java.io.StringReader;
import java.util.List;

import net.jplugin.core.das.route.impl.sqlhandler2.VisitorExpressionManager;
import net.jplugin.core.das.route.impl.sqlhandler2.VisitorForAndExpression;
import net.jplugin.core.das.route.impl.sqlhandler2.AbstractCommandHandler2.KeyFilter;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
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
