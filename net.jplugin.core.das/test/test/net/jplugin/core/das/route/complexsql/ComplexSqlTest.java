package test.net.jplugin.core.das.route.complexsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.route.api.SqlHandleService;
import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.util.SqlParserKit;
import net.sf.jsqlparser.statement.Statement;

public class ComplexSqlTest {
	public void test(){
		try{
			testInner();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public void testInner() throws SQLException{
		String sql = "SELECT  "+
					   "ra.activity_code, "+
					   "ra.activity_name, "+
					   "ra.begin_time, "+
					   "ra.end_time, "+
					   "ra.c_type, "+
//					   "CASE ra.state "+
//					      "WHEN ra.begin_time > now() THEN 0 "+
//					      "WHEN ra.begin_time < now() AND ra.end_time > now() THEN 1 "+
//					      "ELSE 2 "+
//					      "END state, "+

					   "CASE ra.state "+
					      "WHEN 1 THEN 0 "+
					      "WHEN 5 THEN 1 "+
					      "ELSE 2 "+
					      "END state, "+
				      
				      "ra.activity_type, "+
				      "ra.retry_type, "+
				      "ra.create_by, "+
				      "ra.create_time, "+
				      "ra.update_by, "+
				      "ra.update_time "+
					  "FROM "+
					   "retrieve_activity ra "+
					  "WHERE "+
					   "ra.state != - 1 ";
		
		Statement stmt = SqlParserKit.parse(sql);
		System.out.println(stmt);
//		Connection conn = DataSourceFactory.getDataSource("router-db").getConnection();
//		conn = conn.unwrap(RouterConnection.class);
//		List<Object> paras=new ArrayList();
//		SqlHandleResult shr = SqlHandleService.INSTANCE.handle((RouterConnection) conn, sql, paras);
		
		
	}
}
