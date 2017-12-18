package test.net.jplugin.core.das.route.where;

import java.io.StringReader;
import java.util.List;

import net.jplugin.core.das.route.impl.sqlhandler2.VisitorForAndExpression;
import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.route.api.KeyValueForAlgm.Operator;
import net.jplugin.core.das.route.impl.sqlhandler2.AbstractCommandHandler2.KeyFilter;
import net.jplugin.core.das.route.impl.sqlhandler2.VisitorExpressionManager;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class VisitorExpressionManagerTest {

	public void test() throws JSQLParserException{
		String sql;
		List<KeyFilter>  result;
		
		sql = "select * from TB where f1=1";
		result = parse(sql, "f1");
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.EQUAL, result.get(0).operator);

		sql = "select * from TB where f1=1 or f1=2";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.IN, result.get(0).operator);
		AssertKit.assertEqual(1l, result.get(0).value[0].keyConstValue);
		AssertKit.assertEqual(2l, result.get(0).value[1].keyConstValue);

		sql = "select * from TB where f3>5 and ( f1=1 or f1=2)";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.IN, result.get(0).operator);
		AssertKit.assertEqual(1l, result.get(0).value[0].keyConstValue);
		AssertKit.assertEqual(2l, result.get(0).value[1].keyConstValue);
		
		sql = "select * from TB where f3>5 and ( f1=1 or f1=2) and (f1=8 or f5=9)";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.IN, result.get(0).operator);
		AssertKit.assertEqual(1l, result.get(0).value[0].keyConstValue);
		AssertKit.assertEqual(2l, result.get(0).value[1].keyConstValue);
		
		sql = "select * from TB where f1>5 and ( f1=1 or f1=2)";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.BETWEEN, result.get(0).operator);
		AssertKit.assertEqual(5l, result.get(0).value[0].keyConstValue);
		
		sql = "select * from TB where f1>5 and ( f1=1 or (f1=2 or f1=3))";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.BETWEEN, result.get(0).operator);
		AssertKit.assertEqual(5l, result.get(0).value[0].keyConstValue);
		
		sql = "select * from TB where f2>5 and  ( f1=1 or (f1=2 or f1=3))";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(Operator.IN, result.get(0).operator);
		AssertKit.assertEqual(3, result.get(0).value.length);
		AssertKit.assertEqual(1l, result.get(0).value[0].keyConstValue);
		AssertKit.assertEqual(2l, result.get(0).value[1].keyConstValue);
		
		sql = "select * from TB where f2>5 and ( f1=1 or f1=2) and ( f1=1 or f1 in (2,3))";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(2, result.size());
		AssertKit.assertEqual(Operator.IN, result.get(0).operator);
		AssertKit.assertEqual(2l, result.get(0).value[1].keyConstValue);
		AssertKit.assertEqual(Operator.IN, result.get(1).operator);
		AssertKit.assertEqual(3l, result.get(1).value[2].keyConstValue);
		
		
		sql = "select * from TB where f2>5 and (f1=1 or f5=3) and ( f1=1 or f1=2) and ( f1=1 or f1 in (2,3))";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(2, result.size());
		AssertKit.assertEqual(Operator.IN, result.get(0).operator);
		AssertKit.assertEqual(2l, result.get(0).value[1].keyConstValue);
		AssertKit.assertEqual(Operator.IN, result.get(1).operator);
		AssertKit.assertEqual(3l, result.get(1).value[2].keyConstValue);
	}
	
	private void print(String sql,List<KeyFilter> result) {
		System.out.println("\n\nSQL : "+sql);
		for (KeyFilter r:result){
			System.out.println("\t"+r);
		}
		
	}

	private List<KeyFilter> parse(String sql,String key) throws JSQLParserException {
		CCJSqlParserManager pm = new CCJSqlParserManager();
		VisitorForAndExpression wev = new VisitorForAndExpression(key);
		Statement stmt = pm.parse(new StringReader(sql));
		Expression where = ((PlainSelect)((Select)stmt).getSelectBody()).getWhere();
		return VisitorExpressionManager.getKeyFilterList(where, key);
	}
	public static void main(String[] args) throws JSQLParserException {
		new VisitorExpressionManagerTest().test();
	}
}
