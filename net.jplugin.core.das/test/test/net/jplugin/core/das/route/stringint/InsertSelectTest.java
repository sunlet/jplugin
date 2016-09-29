package test.net.jplugin.core.das.route.stringint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"b",1,"b"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"c",1,"c"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('d',1,'d')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('e',?,'e')",new Object[]{1} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('f',?,'f')",new Object[]{1});
		
		AssertKit.assertEqual(3, getCount(connReal,"select count(*) from tb_route0_1"));
		AssertKit.assertEqual(3, getCount(connReal,"select count(*) from tb_route0_2"));
//		AssertKit.assertEqual(6, getCount(conn,"select /*spantable*/ count(*) from tb_route0"));
		
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
