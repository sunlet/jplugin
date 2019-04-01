package test.net.jplugin.core.das.route.where;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.das.route.impl.sqlhandler2.VisitorForAndExpression;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class WhereExpressionVisitorTest {
	
	public void test() throws JSQLParserException{
		
		String sql ;
		RouterKeyFilter kf;
		
		sql = "select * from TB where f1='a'";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.getOperator(), Operator.EQUAL);
		AssertKit.assertEqual(kf.getConstValue()[0], "a");
		
		sql = "select * from TB where f2=f3 and (f2='b' and  (f1='a'))";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.getOperator(), Operator.EQUAL);
		AssertKit.assertEqual(kf.getConstValue()[0], "a");
		
		sql = "select * from TB where f1='a' or f1='b'";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf, null);
		
		sql = "select * from TB where f1='a' and f1='b'";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.getOperator(), Operator.EQUAL);
		AssertKit.assertEqual(kf.getConstValue()[0], "a");
		
		sql = "select * from TB where f1>1 and f1<2";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.getOperator(), Operator.BETWEEN);
		AssertKit.assertEqual(kf.getConstValue()[0], 1l);
		AssertKit.assertEqual(kf.getConstValue()[1], 2l);
		
		sql = "select * from TB where f1>1 and f1>2";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.getOperator(), Operator.BETWEEN);
		AssertKit.assertEqual(kf.getConstValue()[0], 1l);
		AssertKit.assertEqual(kf.getConstValue()[1],null);
		
		sql = "select * from TB where f1 in (1,2,?)";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.getOperator(), Operator.IN);
		AssertKit.assertEqual(kf.getConstValue()[0], 1l);
		AssertKit.assertEqual(kf.getConstValue()[1],2l);
		AssertKit.assertEqual(kf.getConstValue()[2],100L);

		sql = "select * from TB where f1>1 and f1<2 and  f1 in (1,2,?)";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.getOperator(), Operator.IN);
		AssertKit.assertEqual(kf.getConstValue()[0], 1l);
		AssertKit.assertEqual(kf.getConstValue()[1],2l);
		AssertKit.assertEqual(kf.getConstValue()[2],100L);
		
		sql = "select * from TB where f1>1 and f1<2  or  f1 in (1,2,?)";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf,null);
		
		sql = "select * from TB where f1=Mytest(1,Mytest(1,2),3)";
		kf = parse(sql,"TB","f1");
		AssertKit.assertEqual(kf.getOperator(), Operator.EQUAL);
		AssertKit.assertEqual(kf.getConstValue()[0], 7L);
	}

	private RouterKeyFilter parse(String sql,String tb,String key) throws JSQLParserException {
		CCJSqlParserManager pm = new CCJSqlParserManager();
		
		List<Object> params = new ArrayList<>();
		for (int i=0;i<10;i++){
			params.add(100L);
		}
		
		VisitorForAndExpression wev = new VisitorForAndExpression(key,params);
		Statement stmt = pm.parse(new StringReader(sql));
		((PlainSelect)((Select)stmt).getSelectBody()).getWhere().accept(wev);
		return wev.getKnownFilter();
	}
	
	public static void main(String[] args) throws JSQLParserException {
		new WhereExpressionVisitorTest(). test();
	}
}
