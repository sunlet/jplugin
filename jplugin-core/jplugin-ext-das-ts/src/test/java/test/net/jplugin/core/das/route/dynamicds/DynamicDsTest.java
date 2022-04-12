package test.net.jplugin.core.das.route.dynamicds;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.kernel.api.RefAnnotationSupport;

public class DynamicDsTest extends RefAnnotationSupport {

	
	public void test() throws SQLException {
		Connection conn = DataSourceFactory.getDataSource("testdynamicds").getConnection();
		String sql = "select  * from  information_schema.tables where 1=2";
		List<Map<String, String>> list = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(0,list.size());
		System.out.println("ok");
	}

}
