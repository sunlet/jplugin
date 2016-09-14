package test.net.jplugin.core.das.route;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.ObjectRef;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;

public class TestTable1 {

	public void test() throws SQLException {
		DataSource dataSource = DataSourceFactory.getDataSource("router-db");
		Connection conn = dataSource.getConnection();
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"a",1,"a"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"b",1,"b"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"c",1,"c"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('d',1,'d')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('e',?,'e')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('f',?,'f')",null);
		
		AssertKit.assertEqual(3, getCount(conn,"select count(*) from tb_route0_1"));
		AssertKit.assertEqual(3, getCount(conn,"select count(*) from tb_route0_2"));
		
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"a",1,"a"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"b",1,"b"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"c",1,"c"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values('d',1,'d')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values('e',?,'e')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values('f',?,'f')",null );
		
		AssertKit.assertEqual(6, getCount(conn,"select count(*) from tb_route1_2"));
		AssertKit.assertEqual(0, getCount(conn,"select count(*) from tb_route1_1"));
		AssertKit.assertEqual(0, getCount(conn,"select count(*) from tb_route1_3"));
		AssertKit.assertEqual(0, getCount(conn,"select count(*) from tb_route1_4"));
		AssertKit.assertEqual(0, getCount(conn,"select count(*) from tb_route1_5"));
		AssertKit.assertEqual(0, getCount(conn,"select count(*) from tb_route1_6"));
		
		

	}
	
	int getCount(Connection conn,String s){
		ObjectRef<Integer> o = new ObjectRef<>();
		SQLTemplate.executeSelect(conn, s, new IResultDisposer() {
			@Override
			public void readRow(ResultSet rs) throws SQLException {
				o.set(rs.getInt(1));
			}
		},null);
		return o.get();
	}

}
