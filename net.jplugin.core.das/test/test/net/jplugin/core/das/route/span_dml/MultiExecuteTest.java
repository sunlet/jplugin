package test.net.jplugin.core.das.route.span_dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

public class MultiExecuteTest {

	public void test() throws SQLException {
		DataSource dataSource = DataSourceFactory.getDataSource("router-db");
		Connection conn = dataSource.getConnection();
		Connection connReal = DataSourceFactory.getDataSource("database").getConnection();
//		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"a",1,"a"} );
//		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"b",1,null} );
//		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"c",1,"a"} );
//		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('d',1,'d')",null );
//		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('e',?,'e')",new Object[]{1} );
//		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('f',?,'f')",new Object[]{1});
		

		//顺利插入 a c e三条记录
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?),(?,?,?),('e',?,'e')",new Object[]{"a",1,"a","c",1,"a",1});
		
		//顺利插入b d f 三条记录
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?),('d',1,'d'),('f',?,'f')",new Object[]{"b",1,null,1});
		AssertKit.assertEqual(3, getCount(connReal,"select count(*) from tb_route0_1"));
		AssertKit.assertEqual(3, getCount(connReal,"select count(*) from tb_route0_2"));

		PreparedStatement stmt = conn.prepareStatement("delete from tb_route0 where f1 in (?,?)");
		try {
			stmt.setString(1, "a");
			stmt.setString(2, "c");
			AssertKit.assertEqual(2, stmt.executeUpdate());

			stmt.setString(1, "b");
			stmt.setString(2, "d");
			AssertKit.assertEqual(2, stmt.executeUpdate());
			AssertKit.assertEqual(2, stmt.getUpdateCount());

			AssertKit.assertEqual(2, getCount(conn, "select /*spantable*/ count(*) from tb_route0"));
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
		
		//测试UPDATE
//		int num = SQLTemplate.executeUpdateSql(conn, "update /*spantable*/ tb_route0 set f2=2", null);
//		AssertKit.assertEqual(6, num);
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
