package test.net.jplugin.core.das.route.stringint;

import java.io.StringReader;
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
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;

public class LimitTest {

//	public void test() {
//		String sql = "select /*spantable*/ * from tb_route0 limit 1,10";
//		Statement ssmt = pp(sql);
//		System.out.println(ssmt);
//		
//		sql = "select /*spantable*/ * from tb_route0 limit 10";
//		 ssmt = pp(sql);
//		System.out.println(ssmt);
//	}
//
//	public Statement pp(String sql)  {
//		try {
//			CCJSqlParserManager pm = new CCJSqlParserManager();
//			Statement stmt = pm.parse(new StringReader(sql));
//			return stmt;
//		} catch (Exception e) {
//			throw
//			new RuntimeException(e);
//		}
//	}
	
//	public void test() throws SQLException {
//		
//	}

	public void test() throws SQLException {

		// SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3)
		// values(?,?,?)",new Object[]{"a",1,"a"} );
		// SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3)
		// values(?,?,?)",new Object[]{"b",1,null} );
		// SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3)
		// values(?,?,?)",new Object[]{"c",1,"a"} );
		// SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3)
		// values('d',1,'d')",null );
		// SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3)
		// values('e',?,'e')",new Object[]{1} );
		// SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3)
		// values('f',?,'f')",new Object[]{1});

		DataSource dataSource = DataSourceFactory.getDataSource("router-db");
		Connection conn = dataSource.getConnection();
		Connection connReal = DataSourceFactory.getDataSource("database").getConnection();

		List<Map<String, String>> result;
		result = SQLTemplate.executeSelect(conn, "select /*spantable*/ * from tb_route0 ", new Object[] {});
		AssertKit.assertEqual(6, result.size());

		result = SQLTemplate.executeSelect(conn, "select /*spantable*/ * from tb_route0 limit 2", new Object[] {});
		AssertKit.assertEqual(2, result.size());

		// offset 从0 开始
		result = SQLTemplate.executeSelect(conn, "select /*spantable*/ * from tb_route0 limit 1,10", new Object[] {});
		AssertKit.assertEqual(5, result.size());

//		result = SQLTemplate.executeSelect(conn, "select /*spantable*/ * from tb_route0 limit 10 offset 1",
//				new Object[] {});
//		AssertKit.assertEqual(5, result.size());
//
//		result = SQLTemplate.executeSelect(conn, "select /*spantable*/ * from tb_route0 limit 3 offset 1",
//				new Object[] {});
//		AssertKit.assertEqual(3, result.size());

	}

	private void print(List<Map<String, String>> list) {
		for (Map<String, String> map : list) {
			System.out.println();
			for (String key : map.keySet()) {
				System.out.print(key + "=" + map.get(key) + " , ");
			}
		}
	}

	int getCount(Connection conn, String s) {
		return getCount(conn, s, null);
	}

	int getCount(Connection conn, String s, Object[] para) {
		ObjectRef<Integer> o = new ObjectRef<>();
		SQLTemplate.executeSelect(conn, s, new IResultDisposer() {
			@Override
			public void readRow(ResultSet rs) throws SQLException {
				o.set(rs.getInt(1));
			}
		}, para);
		return o.get();
	}
}
