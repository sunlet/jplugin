package net.jplugin.core.das.route.impl.conn.mulqry;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.api.DataSourceFactory;

public class CombineStatementFactory {
	
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
