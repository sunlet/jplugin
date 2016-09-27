package test.net.jplugin.core.das.route.date;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.CalenderKit;
import net.jplugin.common.kits.ObjectRef;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;

public class DateTest {

	public void test() throws SQLException {
		DataSource dataSource = DataSourceFactory.getDataSource("router-db");
		Connection conn = dataSource.getConnection();
		Connection connReal = DataSourceFactory.getDataSource("database").getConnection();
		SQLTemplate.executeInsertSql(conn, "insert into tb_date(f1,f2) values(?,?)",new Object[]{"a",new java.sql.Date(CalenderKit.getTime("20160101010101"))} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_date(f1,f2) values(?,?)",new Object[]{"b",new java.sql.Date(CalenderKit.getTime("20160102010101"))} );
		
		AssertKit.assertEqual(getCount(connReal,"select count(*) from tb_date_160101"), 1);
		AssertKit.assertEqual(getCount(connReal,"select count(*) from tb_date_160102"), 1);
		
		
		SQLTemplate.executeInsertSql(conn, "insert into tb_tisp(f1,f2) values(?,?)",new Object[]{"a",new java.sql.Timestamp(CalenderKit.getTime("20160101010101"))} );
		SQLTemplate.executeInsertSql(conn, "insert into tb_tisp(f1,f2) values(?,?)",new Object[]{"b",new java.sql.Timestamp(CalenderKit.getTime("20160102010101"))} );

		AssertKit.assertEqual(getCount(connReal,"select count(*) from tb_tisp_1601"), 2);
//		AssertKit.assertEqual(getCount(connReal,"select count(*) from tb_tisp_1602"), 0);
		
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
