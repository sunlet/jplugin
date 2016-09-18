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

public class UpdateTest {

	public void test() throws SQLException {
		DataSource dataSource = DataSourceFactory.getDataSource("router-db");
		Connection conn = dataSource.getConnection();
		Connection connReal = DataSourceFactory.getDataSource("database").getConnection();
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"a",1,"a"} );
//		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"a",1,"a"} );
		
		AssertKit.assertEqual(1,SQLTemplate.executeUpdateSql(conn, "update tb_route0 set f2=2 where f1='a'", null));
		AssertKit.assertEqual(1,SQLTemplate.executeUpdateSql(conn, "update tb_route0 set f2=2 where tb_route0.f1='a'", null));
		AssertKit.assertEqual(1,SQLTemplate.executeUpdateSql(conn, "update tb_route0 ttt set f2=2 where ttt.f1='a'", null));

		AssertKit.assertEqual(1,SQLTemplate.executeUpdateSql(conn, "update tb_route0 set f2=2 where f1=?", new Object[]{"a"}));
		AssertKit.assertEqual(1,SQLTemplate.executeUpdateSql(conn, "update tb_route0 set f2=? where f1=?", new Object[]{1,"a"}));
		AssertKit.assertEqual(0,SQLTemplate.executeUpdateSql(conn, "update tb_route0 set f2=? where f1=?", new Object[]{1,"aa"}));
		AssertKit.assertEqual(0,SQLTemplate.executeUpdateSql(conn, "update tb_route0 set f2=2 where f1='aa'", null));
		AssertKit.assertEqual(0,SQLTemplate.executeUpdateSql(conn, "update tb_route0 set f2=2 where f1=?", new Object[]{"aa"}));
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
