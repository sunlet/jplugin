package test.net.jplugin.core.das.route.where;

import java.io.StringReader;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.test;
import net.jplugin.core.das.route.api.KeyValueForAlgm.Operator;
import net.jplugin.core.das.route.impl.sqlhandler2.AbstractCommandHandler2.KeyFilter;
import net.jplugin.core.das.route.impl.sqlhandler2.VisitorForAndExpression;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class WhereExpressionVisitorTest {
	
	public void test() throws JSQLParserException{
		
		String sql ;
		KeyFilter kf;
		
		sql = "select * from TB where f1='a'";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.operator, Operator.EQUAL);
		AssertKit.assertEqual(kf.value[0].keyConstValue, "a");
		AssertKit.assertEqual(kf.value[0].isParamedKey, false);
		
		sql = "select * from TB where f2=f3 and (f2='b' and  (f1='a'))";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.operator, Operator.EQUAL);
		AssertKit.assertEqual(kf.value[0].keyConstValue, "a");
		AssertKit.assertEqual(kf.value[0].isParamedKey, false);
		
		sql = "select * from TB where f1='a' or f1='b'";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf, null);
		
		sql = "select * from TB where f1='a' and f1='b'";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.operator, Operator.EQUAL);
		AssertKit.assertEqual(kf.value[0].keyConstValue, "a");
		AssertKit.assertEqual(kf.value[0].isParamedKey, false);
		
		sql = "select * from TB where f1>1 and f1<2";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.operator, Operator.BETWEEN);
		AssertKit.assertEqual(kf.value[0].keyConstValue, 1l);
		AssertKit.assertEqual(kf.value[0].isParamedKey, false);
		AssertKit.assertEqual(kf.value[1].keyConstValue, 2l);
		AssertKit.assertEqual(kf.value[1].isParamedKey, false);
		
		sql = "select * from TB where f1>1 and f1>2";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.operator, Operator.BETWEEN);
		AssertKit.assertEqual(kf.value[0].keyConstValue, 1l);
		AssertKit.assertEqual(kf.value[0].isParamedKey, false);
		AssertKit.assertEqual(kf.value[1],null);
		
		sql = "select * from TB where f1 in (1,2,?)";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.operator, Operator.IN);
		AssertKit.assertEqual(kf.value[0].keyConstValue, 1l);
		AssertKit.assertEqual(kf.value[0].isParamedKey, false);
		AssertKit.assertEqual(kf.value[1].keyConstValue,2l);
		AssertKit.assertEqual(kf.value[1].isParamedKey, false);
		AssertKit.assertEqual(kf.value[2].keyConstValue,null);
		AssertKit.assertEqual(kf.value[2].isParamedKey, true);

		sql = "select * from TB where f1>1 and f1<2 and  f1 in (1,2,?)";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.operator, Operator.IN);
		AssertKit.assertEqual(kf.value[0].keyConstValue, 1l);
		AssertKit.assertEqual(kf.value[0].isParamedKey, false);
		AssertKit.assertEqual(kf.value[1].keyConstValue,2l);
		AssertKit.assertEqual(kf.value[1].isParamedKey, false);
		AssertKit.assertEqual(kf.value[2].keyConstValue,null);
		AssertKit.assertEqual(kf.value[2].isParamedKey, true);
		
		sql = "select * from TB where f1>1 and f1<2  or  f1 in (1,2,?)";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf,null);
		
		sql = "select * from TB where f1=Mytest(1,2,3)";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.operator, Operator.EQUAL);
		AssertKit.assertEqual(kf.value[0].keyConstValue, 6L);
		AssertKit.assertEqual(kf.value[0].isParamedKey, false);
	}

	private KeyFilter parse(String sql,String tb,String key) throws JSQLParserException {
		CCJSqlParserManager pm = new CCJSqlParserManager();
		VisitorForAndExpression wev = new VisitorForAndExpression(key,null);
		Statement stmt = pm.parse(new StringReader(sql));
		((PlainSelect)((Select)stmt).getSelectBody()).getWhere().accept(wev);
		return wev.getKnownFilter();
	}
	
	public static void main(String[] args) throws JSQLParserException {
		new WhereExpressionVisitorTest(). test();
	}
}
