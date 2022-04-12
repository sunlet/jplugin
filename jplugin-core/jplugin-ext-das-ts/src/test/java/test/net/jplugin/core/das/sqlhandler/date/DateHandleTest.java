package test.net.jplugin.core.das.sqlhandler.date;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.CalenderKit;
import net.jplugin.common.kits.CollectionKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.SqlHandleService;
//import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;

public class DateHandleTest {
	
	
	/**
tb-2-name  =tb_date	
tb-2-algm  =date	
tb-2-key   =f2
tb-2-creation-sql=create table tb_date (f1 varchar(20) primary key,f2 date)


tb-3-name  =tb_tisp	
tb-3-algm  =month	
tb-3-key   =f2
tb-3-creation-sql=create table tb_tisp (f1 varchar(20) primary key,f2 timestamp)

ds-0-name  =router-ds-2	
ds-1-name  = router-ds-1	
	 * @throws SQLException 
	 * 
	 */
	public void test() throws SQLException{
//		RouterConnection conn = DataSourceFactory.getDataSource("router-db").getConnection().unwrap(RouterConnection.class);
		RouterDataSource routerDataSource = (RouterDataSource) DataSourceFactory.getDataSource("router-db").unwrap(RouterDataSource.class);
		String sql;
		SqlHandleResult result;
//		//雨露:
//		2019-10-16 21:08:30
//		20191016210830
//
//		雨露:
//		2019-10-16 01:00:10
		//20191016010010


		//java.sql.Timestamp
		sql = "select * from tb_date where f2=?";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, CollectionKit.listWith(new java.sql.Timestamp(CalenderKit.getTimeFromString("20191016210830").getTime())));
		AssertKit.assertEqual(result._tbCount(),1);
//		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));
		
		//java.sql.Timestamp
		sql = "select * from tb_date where f2=?";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, CollectionKit.listWith(new java.sql.Timestamp(CalenderKit.getTimeFromString("20191016010010").getTime())));
		AssertKit.assertEqual(result._tbCount(),1);
//		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));
		
		
		
		//字符串
		sql = "select * from tb_date where f2='20190101'";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, null);
		AssertKit.assertEqual(result._tbCount(),1);
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));

		
		//long
		sql = "select * from tb_date where f2=?";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, CollectionKit.listWith(CalenderKit.getDateFromString("20190101").getTime()));
		AssertKit.assertEqual(result._tbCount(),1);
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));

		//java.sql.Date
		sql = "select * from tb_date where f2=?";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, CollectionKit.listWith(new java.sql.Date(CalenderKit.getDateFromString("20190101").getTime())));
		AssertKit.assertEqual(result._tbCount(),1);
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));

		//java.sql.Timestamp
		sql = "select * from tb_date where f2=?";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, CollectionKit.listWith(new java.sql.Timestamp(CalenderKit.getDateFromString("20190101").getTime())));
		AssertKit.assertEqual(result._tbCount(),1);
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));
		
		sql = "select * from tb_date where f2=?";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, CollectionKit.listWith(new java.sql.Timestamp(CalenderKit.getTimeFromString("20190101000000").getTime())));
		AssertKit.assertEqual(result._tbCount(),1);
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));
		
		sql = "select * from tb_date where f2=?";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, CollectionKit.listWith(new java.sql.Timestamp(CalenderKit.getTimeFromString("20190101010101").getTime())));
		AssertKit.assertEqual(result._tbCount(),1);
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));
		
		//字符串,多个
		sql = "select * /*spantable*/ from tb_date where f2='20190101' or f2='20190102'";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, null);
		AssertKit.assertEqual(result._tbCount(),2);
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-1","tb_date_190102" ));
		
		//long,多个
		sql = "select * /*spantable*/ from tb_date where f2=? or f2=?";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, CollectionKit.listWith(CalenderKit.getDateFromString("20190101").getTime(),CalenderKit.getDateFromString("20190102").getTime()));
		AssertKit.assertEqual(result._tbCount(),2);
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-1","tb_date_190102" ));

		//java.sql.Date,多个
		sql = "select * /*spantable*/ from tb_date where f2=? or f2=?";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, CollectionKit.listWith(new java.sql.Date(CalenderKit.getDateFromString("20190101").getTime()),new java.sql.Date(CalenderKit.getDateFromString("20190102").getTime())));
		AssertKit.assertEqual(result._tbCount(),2);
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-1","tb_date_190102" ));

		//java.sql.Timestamp,多个
		sql = "select * /*spantable*/ from tb_date where f2=? or f2=?";
		result = SqlHandleService.INSTANCE.handle( routerDataSource, sql, CollectionKit.listWith(new java.sql.Timestamp(CalenderKit.getDateFromString("20190101").getTime()),new java.sql.Timestamp(CalenderKit.getDateFromString("20190102").getTime())));
		AssertKit.assertEqual(result._tbCount(),2);
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_date_190101" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-1","tb_date_190102" ));

	}
}
