package net.jplugin.core.das.route.impl.conn.mulqry;

import java.sql.Connection;

import net.jplugin.core.das.dds.impl.DummyConnection;
//import net.jplugin.core.das.route.impl.conn.RouterConnection;

public class CombineStatementFactory {
	
	
	public static CombinedStatement create(){
		return new CombinedStatement();
	}
	
	public static CombinedPreparedStatement createPrepared(String sql){
		return new CombinedPreparedStatement(sql);
	}
	
//	public static CombinedStatement create(String[] dataSources,String[] sqls){
//		AssertKit.assertEqual(dataSources.length, sqls.length);
//		Statement[] arr = new Statement[dataSources.length];
//		for (String ds:dataSources){
//			DataSource o = DataSourceFactory.getDataSource(ds);
//			Connection conn = o.getConnection();
//			Statement stmt = conn.createStatement();
//			
//		}
//		DataSourceFactory.getDataSource()
//		CombinedStatement cs = new CombinedStatement();
//	}
//	
//	public static CombinedStatement createPrepared(String[] dataSources,String[] sqls){
//		
//	}
}
