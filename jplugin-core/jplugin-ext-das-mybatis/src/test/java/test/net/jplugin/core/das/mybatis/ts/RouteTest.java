package test.net.jplugin.core.das.mybatis.ts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.ObjectRef;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.mybatis.api.MyBatisServiceFactory;
import net.jplugin.core.das.mybatis.impl.IMybatisService;

public class RouteTest {
	public void test() throws SQLException{
		Connection realConn = DataSourceFactory.getDataSource("database").getConnection();
		IMybatisService svc = MyBatisServiceFactory.getService("router-db");
		TbRoute0Mapper m = svc.getMapper(TbRoute0Mapper.class);
		TbRoute0 aaa = null;
		
		m.add("a", 1, "a");
		m.add("b", 1, "b");
		
		AssertKit.assertEqual(1, getCount(realConn, "select count(*) from tb_route0_1"));
		AssertKit.assertEqual(1, getCount(realConn, "select count(*) from tb_route0_2"));
		
		aaa = m.find("a");
		AssertKit.assertEqual(aaa.getF3(), "a");
		
		aaa.setF3("aa");
		m.update(aaa);
		
		aaa = m.find("a");
		AssertKit.assertEqual(aaa.getF3(), "aa");
		
		m.delete("a");
		aaa = m.find("a");
		AssertKit.assertNull(aaa);
		
		
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
