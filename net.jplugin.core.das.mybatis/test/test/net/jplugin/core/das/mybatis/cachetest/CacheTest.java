package test.net.jplugin.core.das.mybatis.cachetest;

import java.awt.List;
import java.sql.Connection;
import java.sql.SQLException;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.PageCond;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.mybatis.api.RefMapper;
import net.jplugin.core.kernel.api.RefAnnotationSupport;

public class CacheTest extends RefAnnotationSupport {
	public static CacheTest INSTANCE = new CacheTest();

	@RefMapper
	ICacheMapper mapper;

	public void test() throws SQLException {
		Connection conn = DataSourceFactory.getDataSource("database").getConnection();
		String sql = "drop table tb_testcache";
		try {
			SQLTemplate.executeDropSql(conn, sql);
		} catch (Exception e) {
		}

		sql = "create table tb_testcache(id int,name varchar(100))";
		SQLTemplate.executeCreateSql(conn, sql);

		mapper.add(1, "name1");
		mapper.add(2, "name2");
		mapper.add(3, "name3");
		mapper.add(4, "name4");

		java.util.List<TestCacheBean> result;
		result = mapper.queryWithPage(new PageCond(2, 1));
		AssertKit.assertEqual(1, result.get(0).getId());
		result = mapper.queryWithPage(new PageCond(2, 2));
		AssertKit.assertEqual(3, result.get(0).getId());
	}
}
