package test.net.jplugin.core.das.route.stringint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.ObjectRef;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;

public class InsertSelectTest {

	public void test() throws SQLException {
		DataSource dataSource = DataSourceFactory.getDataSource("router-db");
		Connection conn = dataSource.getConnection();
		Connection connReal = DataSourceFactory.getDataSource("database").getConnection();
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"a",1,"a"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"b",1,null} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"c",1,"a"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('d',1,'d')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('e',?,'e')",new Object[]{1} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('f',?,'f')",new Object[]{1});
		
		AssertKit.assertEqual(3, getCount(connReal,"select count(*) from tb_route0_1"));
		AssertKit.assertEqual(3, getCount(connReal,"select count(*) from tb_route0_2"));
		
		AssertKit.assertEqual(1, getCount(conn,"select count(*) from tb_route0 where f1='a'"));
		AssertKit.assertEqual(1, getCount(conn,"select count(*) from tb_route0 where f1=?",new Object[]{"a"}));
		AssertKit.assertEqual(1, getCount(conn,"select count(*) from tb_route0 where f1='b'"));
		AssertKit.assertEqual(0, getCount(conn,"select count(*) from tb_route0 where f1='ab'"));
		AssertKit.assertEqual(0, getCount(conn,"select count(*) from tb_route0 where f1=?",new Object[]{"ab"}));
		AssertKit.assertException(new Runnable() {
			@Override
			public void run() {
				int ret = getCount(conn,"select count(*) from tb_route0");
				System.out.println("return :"+ret);
			}
		});
		AssertKit.assertException(new Runnable() {
			@Override
			public void run() {
				int ret = getCount(conn,"select count(*) from tb_route0 where f2=1");
				System.out.println("return :"+ret);
			}
		});
		
		//multi table test for Statement begin>>>>>>>>>
		List<Map<String, String>> list ;
		list = SQLTemplate.executeSelect(conn,"select * from (select *  from tb_route0 where f1='a') bb",null);
		AssertKit.assertEqual(1,list.size());
		
		
		AssertKit.assertEqual(6, getCount(conn,"select  /*spantable*/  count(*) from tb_route0"));
		AssertKit.assertEqual(6, getCount(conn,"select  /*spantable  */  count(0) from tb_route0"));
		AssertKit.assertEqual(3, getCount(conn,"select  /*spantable  */  count(2) from tb_route0"));

		list = SQLTemplate.executeSelect(conn, "select /*spantable*/ count(*) from (select  *  from tb_route0) tb1", null);
		AssertKit.assertEqual("6", list.get(0).get("count(*)"));
		list = SQLTemplate.executeSelect(conn, "select  count(*)/*spantable*/ from (select  *  from tb_route0) tb1", null);
		AssertKit.assertEqual("6", list.get(0).get("count(*)"));

		list = SQLTemplate.executeSelect(conn, "select count(*) from (select /*spantable*/ *  from tb_route0) tb1", null);
		AssertKit.assertEqual("6", list.get(0).get("count(*)"));

		list = SQLTemplate.executeSelect(conn, "select /*spantable*/ *  from tb_route0", null);
		print(list);
		AssertKit.assertEqual(6, list.size());

		list = SQLTemplate.executeSelect(conn, "select * from (select /*spantable*/ *  from tb_route0 where f1<>'b' order by f3) t1", null);
		AssertKit.assertEqual(5, list.size());
		
		list = SQLTemplate.executeSelect(conn, "select /*spantable*/ *  from tb_route0 where f1<>'b' order by f1", null);
		AssertKit.assertEqual(5, list.size());
		AssertKit.assertEqual(list.get(0).get("f1"), "a");
		AssertKit.assertEqual(list.get(1).get("f1"), "c");
		AssertKit.assertEqual(list.get(2).get("f1"), "d");
		AssertKit.assertEqual(list.get(3).get("f1"), "e");
		AssertKit.assertEqual(list.get(4).get("f1"), "f");

		list = SQLTemplate.executeSelect(conn, "select /*spantable*/ *  from tb_route0 where f1<>'b' order by f1 desc", null);
		AssertKit.assertEqual(5, list.size());
		AssertKit.assertEqual(list.get(4).get("f1"), "a");
		AssertKit.assertEqual(list.get(3).get("f1"), "c");
		AssertKit.assertEqual(list.get(2).get("f1"), "d");
		AssertKit.assertEqual(list.get(1).get("f1"), "e");
		AssertKit.assertEqual(list.get(0).get("f1"), "f");

		list = SQLTemplate.executeSelect(conn, "select /*spantable*/ *  from tb_route0 order by f3 asc", null);
		AssertKit.assertEqual(6, list.size());
		AssertKit.assertEqual(list.get(0).get("f3"), null);
		AssertKit.assertEqual(list.get(1).get("f3"), "a");

		list = SQLTemplate.executeSelect(conn, "select /*spantable*/ *  from tb_route0 order by f3 desc", null);
		AssertKit.assertEqual(6, list.size());
		AssertKit.assertEqual(list.get(5).get("f3"), null);
		AssertKit.assertEqual(list.get(0).get("f3"), "f");
		
		list = SQLTemplate.executeSelect(conn, "select /*spantable*/ *  from tb_route0  order by f3,f1", null);
		AssertKit.assertEqual(6, list.size());
		AssertKit.assertEqual(list.get(0).get("f1"), "b");
		AssertKit.assertEqual(list.get(1).get("f1"), "a");
		AssertKit.assertEqual(list.get(2).get("f1"), "c");

		//multi table for statement test end>>>>>>>>>>>
		
		//multi table test for Prepared Statement begin>>>>>>>>>
		list = SQLTemplate.executeSelect(conn,"select * from (select *  from tb_route0 where f1=?) bb",new Object[]{"a"});
		AssertKit.assertEqual(1,list.size());
		
		
		list = SQLTemplate.executeSelect(conn, "select * from (select /*spantable*/ *  from tb_route0 where f1<>? order by f3) t1", new Object[]{"b"});
		AssertKit.assertEqual(5, list.size());
		
		list = SQLTemplate.executeSelect(conn, "select /*spantable*/ *  from tb_route0 where f1<>? order by f1", new Object[]{"b"});
		AssertKit.assertEqual(5, list.size());
		AssertKit.assertEqual(list.get(0).get("f1"), "a");
		AssertKit.assertEqual(list.get(1).get("f1"), "c");
		AssertKit.assertEqual(list.get(2).get("f1"), "d");
		AssertKit.assertEqual(list.get(3).get("f1"), "e");
		AssertKit.assertEqual(list.get(4).get("f1"), "f");
		//multi table for prepared statement test end>>>>>>>>>>>
		
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"a",1,"a"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"b",1,"b"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"c",1,"c"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values('d',1,'d')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values('e',?,'e')",new Object[]{1} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values('f',?,'f')",new Object[]{1} );
		
		AssertKit.assertEqual(6, getCount(connReal,"select count(*) from tb_route1_2"));
		AssertKit.assertEqual(0, getCount(connReal,"select count(*) from tb_route1_1"));
		AssertKit.assertEqual(0, getCount(connReal,"select count(*) from tb_route1_3"));
		AssertKit.assertEqual(0, getCount(connReal,"select count(*) from tb_route1_4"));
		AssertKit.assertEqual(0, getCount(connReal,"select count(*) from tb_route1_5"));
		
		AssertKit.assertEqual(6, getCount(conn,"select count(*) from tb_route1 where f2=1"));
		AssertKit.assertEqual(0, getCount(conn,"select count(*) from tb_route1 where f2=2"));
		AssertKit.assertEqual(6, getCount(conn,"select count(*) from tb_route1 where f2=?",new Object[]{1}));
		AssertKit.assertEqual(0, getCount(conn,"select count(*) from tb_route1 where f2=?",new Object[]{11}));
		AssertKit.assertException(new Runnable() {
			@Override
			public void run() {
				AssertKit.assertEqual(6, getCount(conn,"select count(*) from tb_route1 where f3='a'"));
			}
		});
	}

	private void print(List<Map<String, String>> list) {
		for (Map<String, String> map:list){
			System.out.println();
			for (String key:map.keySet()){
				System.out.print(key+"="+map.get(key)+" , ");
			}
		}
	}

	int getCount(Connection conn,String s){
		return getCount(conn,s,null);
	}

	int getCount(Connection conn,String s,Object[] para){
		ObjectRef<Integer> o = new ObjectRef<>();
		SQLTemplate.executeSelect(conn, s, new IResultDisposer() {
			@Override
			public void readRow(ResultSet rs) throws SQLException {
				o.set(rs.getInt(1));
			}
		},para);
		return o.get();
	}

}
