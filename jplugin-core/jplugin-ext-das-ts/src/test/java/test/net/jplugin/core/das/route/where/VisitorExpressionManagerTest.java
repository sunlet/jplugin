package test.net.jplugin.core.das.route.where;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.das.route.impl.sqlhandler2.VisitorExpressionManager;
import net.jplugin.core.das.route.impl.sqlhandler2.VisitorForAndExpression;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class VisitorExpressionManagerTest {

	public void test() throws JSQLParserException{
		String sql;
		List<RouterKeyFilter>  result;
		
		sql = "select * from TB where f1=1";
		result = parse(sql, "f1");
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.EQUAL, result.get(0).getOperator());

		sql = "select * from TB where f1=1 or f1=2";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.IN, result.get(0).getOperator());
		AssertKit.assertEqual(1l, result.get(0).getConstValue()[0]);
		AssertKit.assertEqual(2l, result.get(0).getConstValue()[1]);

		sql = "select * from TB where f3>5 and ( f1=1 or f1=2)";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.IN, result.get(0).getOperator());
		AssertKit.assertEqual(1l, result.get(0).getConstValue()[0]);
		AssertKit.assertEqual(2l, result.get(0).getConstValue()[1]);
		
		sql = "select * from TB where f3>5 and ( f1=1 or f1=2) and (f1=8 or f5=9)";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.IN, result.get(0).getOperator());
		AssertKit.assertEqual(1l, result.get(0).getConstValue()[0]);
		AssertKit.assertEqual(2l, result.get(0).getConstValue()[1]);
		
		sql = "select * from TB where f1>5 and ( f1=1 or f1=2)";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.BETWEEN, result.get(0).getOperator());
		AssertKit.assertEqual(5l, result.get(0).getConstValue()[0]);
		
		sql = "select * from TB where f1>5 and ( f1=1 or (f1=2 or f1=3))";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(1, result.size());
		AssertKit.assertEqual(Operator.BETWEEN, result.get(0).getOperator());
		AssertKit.assertEqual(5l, result.get(0).getConstValue()[0]);
		
		sql = "select * from TB where f2>5 and  ( f1=1 or (f1=2 or f1=3))";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(Operator.IN, result.get(0).getOperator());
		AssertKit.assertEqual(3, result.get(0).getConstValue().length);
		AssertKit.assertEqual(1l, result.get(0).getConstValue()[0]);
		AssertKit.assertEqual(2l, result.get(0).getConstValue()[1]);
		
		sql = "select * from TB where f2>5 and ( f1=1 or f1=2) and ( f1=1 or f1 in (2,3))";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(2, result.size());
		AssertKit.assertEqual(Operator.IN, result.get(0).getOperator());
		AssertKit.assertEqual(2l, result.get(0).getConstValue()[1]);
		AssertKit.assertEqual(Operator.IN, result.get(1).getOperator());
		AssertKit.assertEqual(3l, result.get(1).getConstValue()[2]);
		
		
		sql = "select * from TB where f2>5 and (f1=1 or f5=3) and ( f1=1 or f1=2) and ( f1=1 or f1 in (2,3))";
		result = parse(sql, "f1");
		print(sql,result);
		AssertKit.assertEqual(2, result.size());
		AssertKit.assertEqual(Operator.IN, result.get(0).getOperator());
		AssertKit.assertEqual(2l, result.get(0).getConstValue()[1]);
		AssertKit.assertEqual(Operator.IN, result.get(1).getOperator());
		AssertKit.assertEqual(3l, result.get(1).getConstValue()[2]);
	}
	
	private void print(String sql,List<RouterKeyFilter> result) {
		System.out.println("\n\nSQL : "+sql);
		for (RouterKeyFilter r:result){
			System.out.println("\t"+r);
		}
		
	}

	private List<RouterKeyFilter> parse(String sql,String key) throws JSQLParserException {
		CCJSqlParserManager pm = new CCJSqlParserManager();
		VisitorForAndExpression wev = new VisitorForAndExpression(key,null);
		Statement stmt = pm.parse(new StringReader(sql));
		Expression where = ((PlainSelect)((Select)stmt).getSelectBody()).getWhere();
		
		List<Object> params = new ArrayList<>();
		for (int i=0;i<10;i++){
			params.add(100L);
		}
		return VisitorExpressionManager.getKeyFilterList(where, key,params);
	}
	public static void main(String[] args) throws JSQLParserException {
		new VisitorExpressionManagerTest().test();
	}
}
