package test.net.jplugin.core.das.sqlhandler.date;

import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.SqlHandleService;
//import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;

public class HashHandlerTest {
/**
 * tb-1-name  =tb_route1	
tb-1-algm  =hash	
tb-1-splits=5	
tb-1-key   =f2	
 * @throws SQLException 
 */
	public void test() throws SQLException{
//		RouterConnection conn = DataSourceFactory.getDataSource("router-db").getConnection().unwrap(RouterConnection.class);
		RouterDataSource datsSource = (RouterDataSource) DataSourceFactory.getDataSource("router-db").unwrap(RouterDataSource.class);
		String sql;
		SqlHandleResult result;
		
		sql = "select /*spantable*/ * from tb_route1 where f2 in (0,1,2,3,4)";
		result = SqlHandleService.INSTANCE.handle( datsSource, sql, null);
		AssertKit.assertEqual(result._tbCount(),5);
		AssertKit.assertTrue(result._containDsTb("router-ds-1","tb_route1_1" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-1","tb_route1_3" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-1","tb_route1_5" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_route1_2" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_route1_4" ));
		
		sql = "select /*spantable*/ * from tb_route1 where f2";
		result = SqlHandleService.INSTANCE.handle( datsSource, sql, null);
		AssertKit.assertEqual(result._tbCount(),5);
		AssertKit.assertTrue(result._containDsTb("router-ds-1","tb_route1_1" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-1","tb_route1_3" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-1","tb_route1_5" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_route1_2" ));
		AssertKit.assertTrue(result._containDsTb("router-ds-2","tb_route1_4" ));
		
//		System.out.println(new Integer(0).hashCode());
//		System.out.println(new Integer(10).hashCode());
		
	}
}
