package test.net.jplugin.core.das.route.groupby;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.ObjectRef;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;

public class DBGroupByTest {

	Connection conn;
	Connection connReal;
	
	public void test() throws SQLException{
		conn = DataSourceFactory.getDataSource("router-db").getConnection();
		connReal = DataSourceFactory.getDataSource("database").getConnection();
		initData();
		runtest();
	}
	private void initData() throws SQLException {
		SQLTemplate.executeInsertSql(conn, "insert into tb_groupby(f1,f2,f3) values('a',1,'x')",null);
		SQLTemplate.executeInsertSql(conn, "insert into tb_groupby(f1,f2,f3) values('b',1,'x')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_groupby(f1,f2,f3) values('c',1,'x')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_groupby(f1,f2,f3) values('d',1,'x')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_groupby(f1,f2,f3) values('e',1,'y')",null);
		SQLTemplate.executeInsertSql(conn, "insert into tb_groupby(f1,f2,f3) values('f',1,'y')",null);
		SQLTemplate.executeInsertSql(conn, "insert into tb_groupby(f1,f2,f3) values('a',2,'y')",null);
		SQLTemplate.executeInsertSql(conn, "insert into tb_groupby(f1,f2,f3) values('b',2,'y')",null );
		SQLTemplate.executeInsertSql(conn, "insert into tb_groupby(f1,f2,f3) values('c',2,'y')",null );
		
		AssertKit.assertEqual(1,getCount(connReal, "select count(*) from tb_groupby_1"));
		AssertKit.assertEqual(3,getCount(connReal, "select count(*) from tb_groupby_2"));
		AssertKit.assertEqual(3,getCount(connReal, "select count(*) from tb_groupby_3"));
		AssertKit.assertEqual(2,getCount(connReal, "select count(*) from tb_groupby_4"));
		AssertKit.assertEqual(9,getCount(conn, "select /*spantable*/ count(*) from tb_groupby"));
	}

	private void runtest() {
		String sql= "select /*spantable*/ count(*),f3 from tb_groupby group by f3";
		List<Map<String, String>> result = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(findMap(result,"f3","x").get("count(*)"), "4");
		AssertKit.assertEqual(findMap(result,"f3","y").get("count(*)"), "5");
		print(result);
		
		sql= "select /*spantable*/ sum(f2),f3 from tb_groupby group by f3";
		result = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(findMap(result,"f3","x").get("sum(f2)"), "4");
		AssertKit.assertEqual(findMap(result,"f3","y").get("sum(f2)"), "8");
		
		
		sql= "select /*spantable*/ max(f2),f3 from tb_groupby group by f3";
		result = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(findMap(result,"f3","x").get("max(f2)"), "1");
		AssertKit.assertEqual(findMap(result,"f3","y").get("max(f2)"), "2");
		
		print(result);
		
		sql= "select /*spantable*/ min(f2),f3 from tb_groupby group by f3";
		result = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(findMap(result,"f3","x").get("min(f2)"), "1");
		AssertKit.assertEqual(findMap(result,"f3","y").get("min(f2)"), "1");
		
		print(result);
		
		sql= "select /*spantable*/ avg(f2),f3 from tb_groupby group by f3";
		result = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(findMap(result,"f3","x").get("avg(f2)"), "1.0000");
		AssertKit.assertEqual(findMap(result,"f3","y").get("avg(f2)"), "1.6000");
		
		print(result);
		
		sql= "select /*spantable*/ avg(f2),f3 from  tb_groupby where 1=2 group by f3";
		result = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(result.size(),0);
		
		print(result);
		
		//测试orderby
		sql= "select /*spantable*/ avg(f2),f3 from tb_groupby group by f3 order by f3 desc";
		result = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(result.get(0).get("f3"),"y");
		AssertKit.assertEqual(result.get(1).get("avg(f2)"), "1.0000");
		AssertKit.assertEqual(result.get(0).get("avg(f2)"), "1.6000");

		print(result);
		
		sql= "select /*spantable*/ avg(f2),f3 from tb_groupby group by f3 order by f3 asc";
		result = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(result.get(1).get("f3"),"y");
		AssertKit.assertEqual(result.get(0).get("avg(f2)"), "1.0000");
		AssertKit.assertEqual(result.get(1).get("avg(f2)"), "1.6000");

		print(result);
		
		sql= "select /*spantable*/ avg(f2),f1 from tb_groupby group by f1 order by f1 desc";
		result = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(result.get(0).get("f1"),"f");
		AssertKit.assertEqual(result.get(1).get("f1"),"e");
		AssertKit.assertEqual(result.get(2).get("f1"),"d");
		AssertKit.assertEqual(result.size(),6);
		
		sql= "select /*spantable*/ avg(f2),f1 from tb_groupby group by f1 order by f1 ";
		result = SQLTemplate.executeSelect(conn, sql, null);
		AssertKit.assertEqual(result.get(0).get("f1"),"a");
		AssertKit.assertEqual(result.get(1).get("f1"),"b");
		AssertKit.assertEqual(result.get(2).get("f1"),"c");
		AssertKit.assertEqual(result.size(),6);

		print(result);
	}
	
	
	private Map<String, String> findMap(List<Map<String,String>> list, String field, String v) {
		for (Map<String, String> m:list){
			if (v.equals(m.get(field))){
				return m;
			}
		}
		return null;
	}
	private void print(List<Map<String, String>> list) {
		for (Map<String, String> map:list){
			System.out.println();
			for (String key:map.keySet()){
				System.out.print(key+"="+map.get(key)+" , ");
			}
		}
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
