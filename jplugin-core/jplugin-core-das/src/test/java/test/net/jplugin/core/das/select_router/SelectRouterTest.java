package test.net.jplugin.core.das.select_router;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.SQLTemplate;

public class SelectRouterTest {

	public void test() throws SQLException {
//		DbCreate4Router.drop();
//		DbCreate4Router.createDb();
		DbCreate4Router.create();
		
		testRouter();
		
		testAutoGenKey();
		
	}

	private void testAutoGenKey() throws SQLException {
		DataSource dsa = DataSourceFactory.getDataSource("the_db_a");
		DataSource dsb = DataSourceFactory.getDataSource("the_db_b");
		DataSource dsc = DataSourceFactory.getDataSource("the_db_c");
		DataSource ds_select = DataSourceFactory.getDataSource("the_select_db");
		try {
			SQLTemplate.executeDropSql(dsc.getConnection(),"drop table test_autogen");
		}catch(Exception e) {}
		try{
			SQLTemplate.executeCreateSql(dsc.getConnection(), "create table test_autogen (f1 integer PRIMARY KEY AUTO_INCREMENT ,f2 varchar(60))");
		}catch(Exception e) {
			
		}
		
		List<Long> key = SQLTemplate.executeInsertReturnGenKey(ds_select.getConnection(), "insert into test_autogen(f2) values (?)", new Object[] {"vvv"});
		System.out.println("key="+key.get(0));
		
		key = SQLTemplate.executeInsertReturnGenKey(ds_select.getConnection(), "insert into test_autogen(f2) values ('vvvv')", null);
		System.out.println("key="+key.get(0));
		
		key = SQLTemplate.executeInsertReturnGenKey(dsc.getConnection(), "insert into test_autogen(f2) values ('vvvv')", null);
		System.out.println("key="+key);
		
		
	}

	private void testRouter() throws SQLException {
		DataSource dsa = DataSourceFactory.getDataSource("the_db_a");
		DataSource dsb = DataSourceFactory.getDataSource("the_db_b");
		DataSource dsc = DataSourceFactory.getDataSource("the_db_c");
		
		DataSource ds_select = DataSourceFactory.getDataSource("the_select_db");
		
		SQLTemplate.executeInsertSql(ds_select.getConnection(), "insert into the_tb1(f1) values ('a')", null);
		List<Map<String, String>> result = SQLTemplate.executeSelect(ds_select.getConnection(), "select count(*) c from the_tb1", null);
		AssertKit.assertEqual("1",result.get(0).get("c")+"");
		
		SQLTemplate.executeInsertSql(ds_select.getConnection(), "insert into the_tb2(f1) values ('a')", null);
		result = SQLTemplate.executeSelect(ds_select.getConnection(), "select count(*) c from the_tb2", null);
		AssertKit.assertEqual("1",result.get(0).get("c")+"");
		
		SQLTemplate.executeInsertSql(ds_select.getConnection(), "insert into the_tb3(f1) values ('a')", null);
		result = SQLTemplate.executeSelect(ds_select.getConnection(), "select count(*) c from the_tb3", null);
		AssertKit.assertEqual("1",result.get(0).get("c")+"");
		
		
		
	}
}
