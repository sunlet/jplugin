package test.net.jplugin.core.mtenant.sqlhandlervisit;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.mtenant.handler2.SqlHandlerVisitor;
import net.sf.jsqlparser.JSQLParserException;

public class SqlHandlerVisitorTest {
//	static String cloumnName = "column1";
	static String cloumnName = "";
	
	public static void main(String[] args) throws JSQLParserException {
		testSelect();
		testInsert();
		testUpdate();
		testDelete();
//		testPerform();
	}
	
	private static void testPerform() throws JSQLParserException {
		SqlHandlerVisitor v = new SqlHandlerVisitor("sssss",cloumnName,"1001");
		String from,to;
		
		long start = System.currentTimeMillis();
		for (int i=0;i<10000;i++){
			from = "select * from table1,table2 left join t3 f WHERE A=1 OR C=3 and f1 in (select * from t3,t4 where t3.x = t4.y)";
			to = v.handle(from);
		}
		System.out.println("timeis :"+(System.currentTimeMillis() - start));
	}

	private static void testDelete() throws JSQLParserException {
		SqlHandlerVisitor v = new SqlHandlerVisitor("sssss",cloumnName,"1001");
		String from,to;
		
		from = "delete from t1 ";
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		from = "delete from t1  where f1=1 and f2=2";
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
	}

	static void testUpdate() throws JSQLParserException{
		SqlHandlerVisitor v = new SqlHandlerVisitor("sssss",cloumnName,"1001");
		String from,to;
		
		from = "update t1 set f1=1,f2=2";
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		from = "update t1 set f1=1,f2=2 where 1=2";
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
	}

	private static void testInsert() throws JSQLParserException {
		SqlHandlerVisitor v = new SqlHandlerVisitor("sssss",cloumnName,"1001");
		String from,to;
		
		from = "insert into t1 (f1,f2) values (?,'a')";
		to = v.handle(from);
//		AssertKit.assertEqual("INSERT INTO T1 (f1, f2, column1) VALUES (?, 'a', '1001')", to);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		from = "insert into t1 (f1,f2) select a,b from (select * from t5 where 1=2)";
		to = v.handle(from);
//		AssertKit.assertEqual("INSERT INTO T1 (f1, f2, column1) VALUES (?, 'a', '1001')", to);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
	}

	private static void testSelect() throws JSQLParserException {
		SqlHandlerVisitor v = new SqlHandlerVisitor("sssss",cloumnName,"1001");
		String from,to;
		
		from = "select * from t1 t where f1=1 and f2=2 or f3=3 and f4=4";
		to = v.handle(from);
//		AssertKit.assertEqual("SELECT * FROM sssss.table1, sssss.table2 LEFT JOIN sssss.t3 AS f ON f.column1 = '1001' WHERE sssss.table2.column1 = '1001' AND sssss.table1.column1 = '1001' AND (A = 1 OR C = 3 AND f1 IN (SELECT * FROM sssss.t3, sssss.t4 WHERE sssss.t4.column1 = '1001' AND sssss.t3.column1 = '1001' AND (t3.x = t4.y)))", to);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		
		from = "select * from table1,table2 left join t3 f WHERE A=1 OR C=3 and f1 in (select * from t3,t4 where t3.x = t4.y)";
		to = v.handle(from);
//		AssertKit.assertEqual("SELECT * FROM sssss.table1, sssss.table2 LEFT JOIN sssss.t3 AS f ON f.column1 = '1001' WHERE sssss.table2.column1 = '1001' AND sssss.table1.column1 = '1001' AND (A = 1 OR C = 3 AND f1 IN (SELECT * FROM sssss.t3, sssss.t4 WHERE sssss.t4.column1 = '1001' AND sssss.t3.column1 = '1001' AND (t3.x = t4.y)))", to);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
	}
}
