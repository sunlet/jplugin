package test.net.jplugin.core.das.select_router;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.SQLTemplate;

public class SelectRouterTableNotCfgTest {

	public void test() throws SQLException {
//		DbCreate4Router.drop();
//		DbCreate4Router.createDb();
		DbCreate4RouterNotCfgTable.create();

		testRouter();

	}


	private void testRouter() throws SQLException {
		DataSource qry_ds = DataSourceFactory.getDataSource("the_db_a");
		DataSource modify_ds = DataSourceFactory.getDataSource("the_db_b");
		
		DataSource ds_select = DataSourceFactory.getDataSource("the_select_db_table_not_cfg");
		
		//插入一条
		SQLTemplate.executeInsertSql(ds_select.getConnection(), "insert into the_tb_notcfg(f1) values ('a')", null);
		
		//在modify_ds可以查询到
		List<Map<String, String>> result = SQLTemplate.executeSelect(modify_ds.getConnection(), "select count(*) c from the_tb_notcfg", null);
		AssertKit.assertEqual("1",result.get(0).get("c")+"");
		
		//ds_select无法查询到
		result = SQLTemplate.executeSelect(ds_select.getConnection(), "select count(*) c from the_tb_notcfg", null);
		AssertKit.assertEqual("0",result.get(0).get("c")+"");

		//qry_ds无法查询到
		result = SQLTemplate.executeSelect(qry_ds.getConnection(), "select count(*) c from the_tb_notcfg", null);
		AssertKit.assertEqual("0",result.get(0).get("c")+"");

		
	}
}
