package test.net.jplugin.das.hib;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.api.stat.CreateIndexStatement;
import net.jplugin.core.das.api.stat.DeleteStatement;
import net.jplugin.core.das.api.stat.InsertStatement;
import net.jplugin.core.das.api.stat.SelectStatement;
import net.jplugin.core.das.api.stat.UpdateStatement;

public class StatementTest {

	public static void test() {
		testSelect();
		testInsert();
		testUpdate();
		testDelete();
		testCreateIndex();
	}

	private static void testCreateIndex() {
		CreateIndexStatement cis = CreateIndexStatement.create();
		cis.setTableName("tb1");
		cis.addColumn("col1");
		cis.addColumn("col2");
		cis.setIndexName("idx1");
		
		System.out.println(cis.getSqlClause());
		System.out.println(cis.getParams());
		
	}

	private static void testDelete() {
		DeleteStatement ds = DeleteStatement.create("tb1", "f1=2 and f2=3", "o1","o2");
		ds.addWhere("and f3=?", "f3value");
		System.out.println(ds.getSqlClause());
		System.out.println(ds.getParams());
	}

	private static void testUpdate() {
		final UpdateStatement us = UpdateStatement.create();
		us.setTableName("tb1");
		us.addItemPair("f1", "f1value");
		us.addItemPair("f2", "f2value");
		us.addWhere("1=?","b1");
		
		System.out.println(us.getSqlClause());
		System.out.println(us.getParams());
		
		us.setSetString("f1=1,f2=2");
		AssertKit.assertException(new Runnable() {
			
			public void run() {
				us.getSqlClause();
			}
		});
	}

	private static void testInsert() {
		final InsertStatement is = InsertStatement.create("tb1", "f1,f2","?,?" , "o1","o2");
		System.out.println(is.getSqlClause());
		System.out.println(is.getParams());
		
		is.addValue("vv");
		AssertKit.assertException(new Runnable() {
			
			public void run() {
				is.getSqlClause();
			}
		});
	}

	private static void testSelect() {
		final SelectStatement ss = SelectStatement.create("user","f1,f2,f3","f1=1 and f2=2");
		System.out.println(ss.getSqlClause());
		
		ss.addFrom("u1", "a");
		System.out.println(ss.getSqlClause());
		
		ss.addWhere("and f3=3", "a");
		System.out.println(ss.getSqlClause());
		System.out.println(ss.getParams());
		
		SelectStatement ss2 = SelectStatement.create();
		ss2.addFrom("tb", "");
		ss2.addWhere("1=2", "b");
		ss2.addItemPair("f11","_f");
		
		ss.addWhere("and f4 in", null);
		ss.addSubQryWhere(ss2);
		
		ss.setGroupby("s1,s2");
		ss.setHaving("count(s1)>100");
		ss.setOrderBy("s1");
		
		System.out.println(ss.getSqlClause());
		System.out.println(ss.getParams());
		
		ss.addItemPair("f1", null);
		AssertKit.assertException(new Runnable() {
			public void run() {
				ss.getSqlClause();
			}
		});
	}

}
