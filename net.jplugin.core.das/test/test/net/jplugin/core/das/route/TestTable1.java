package test.net.jplugin.core.das.route;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.SQLTemplate;

public class TestTable1 {

	public void test() throws SQLException {
		DataSource dataSource = DataSourceFactory.getDataSource("router-db");
		Connection conn = dataSource.getConnection();
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"a",1,"a"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"b",1,"b"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"c",1,"c"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"d",1,"d"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"e",1,"e"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values(?,?,?)",new Object[]{"f",1,"f"} );

		
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"a",1,"a"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"b",1,"b"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"c",1,"c"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"d",1,"d"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"e",1,"e"} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_route1(f1,f2,f3) values(?,?,?)",new Object[]{"f",1,"f"} );

	}

}
