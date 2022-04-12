package test.net.jplugin.core.das.router.dsnamed;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.dds.kits.SqlParserKit;

public class DsNamedDataSourceTest {

	DataSource database = DataSourceFactory.getDataSource("database");
	DataSource dsc = DataSourceFactory.getDataSource("the_db_c");
	DataSource ds_select = DataSourceFactory.getDataSource("the_select_db");
	
	DataSource ds_named_ds = DataSourceFactory.getDataSource("ds_named_ds");
	
	public void test() throws SQLException {
		createTb();
		testCommon();
		testInsertSelect();
	}

	private void testInsertSelect() throws SQLException {
		SQLTemplate.executeInsertSql(ds_named_ds.getConnection(), "insert into database.NamedTb2(f1,f2) select f1,f2 from the_db_c.NamedTb1", null);
		
		List<Map<String, String>> result = SQLTemplate.executeSelect(ds_named_ds.getConnection(), "select * from database.NamedTb2", null);
		AssertKit.assertEqual(result.size(), 4);
		
		SQLTemplate.executeInsertSql(ds_named_ds.getConnection(), "insert into database.NamedTb2  select f1,f2 from the_db_c.NamedTb1", null);
		
		result = SQLTemplate.executeSelect(ds_named_ds.getConnection(), "select * from database.NamedTb2", null);
		AssertKit.assertEqual(result.size(), 6);
	}

	private void testCommon() throws SQLException {
		//通过原始数据源插入3个
		SQLTemplate.executeInsertSql(dsc.getConnection(), "insert into NamedTb1 values(10,'a')", null);
		SQLTemplate.executeInsertSql(dsc.getConnection(), "insert into NamedTb1 values(11,'b')", null);
		SQLTemplate.executeInsertSql(database.getConnection(), "insert into NamedTb2 values(20,'c')", null);
		
		List<Map<String, String>> result = SQLTemplate.executeSelect(ds_named_ds.getConnection(), "select * from the_db_c.NamedTb1", null);
		AssertKit.assertEqual(result.size(), 2);
		
		result = SQLTemplate.executeSelect(ds_named_ds.getConnection(), "select * from database.NamedTb2 where ?=?", new Object[] {1,1});
		AssertKit.assertEqual(result.size(), 1);

		//通过namedds数据源插入1个
		SQLTemplate.executeInsertSql(ds_named_ds.getConnection(), "insert into database.NamedTb2 values(21,'d')", null);
		
		result = SQLTemplate.executeSelect(ds_named_ds.getConnection(), "select * from database.NamedTb2 where ?=?", new Object[] {1,1});
		AssertKit.assertEqual(result.size(), 2);
	}

	private void createTb() throws SQLException {
		try {
			SQLTemplate.executeDropSql(dsc.getConnection(), "drop table NamedTb1");
		}catch(Exception e) {}
		
		try {
			SQLTemplate.executeDropSql(database.getConnection(), "drop table NamedTb2");
		}catch(Exception e) {}
		
		SQLTemplate.executeCreateSql(dsc.getConnection(), "create table NamedTb1(f1 integer,f2 varchar(20))");
		SQLTemplate.executeCreateSql(database.getConnection(), "create table NamedTb2(f1 integer,f2 varchar(20))");
		
	}
}
