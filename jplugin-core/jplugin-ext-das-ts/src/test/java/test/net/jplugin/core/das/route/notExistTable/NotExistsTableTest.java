package test.net.jplugin.core.das.route.notExistTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.ObjectRef;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.route.impl.autocreate.TableExistsMaintainer;
import net.jplugin.core.das.route.impl.autocreate.TableExistsMaintainer.MaintainReturn;
import test.net.jplugin.core.das.route.stringint.DbCreateStringInt;

public class NotExistsTableTest {

	public void test() throws SQLException {
		DataSource dataSource = DataSourceFactory.getDataSource("router-db");
		Connection conn = dataSource.getConnection();
		Connection connReal = DataSourceFactory.getDataSource("database").getConnection();
		
		
		//验证自动建表
		DbCreateStringInt.drop();
		TableExistsMaintainer.lastMaintainResult = null;
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('a',1,'a')",new Object[]{} );
		AssertKit.assertEqual(TableExistsMaintainer.lastMaintainResult.isReturnZeroRowUpdateStatement(),false);
		AssertKit.assertEqual(TableExistsMaintainer.lastMaintainResult.getTargetSqlForDummy(),null);
		AssertKit.assertEqual(1, getCount(conn,"select/*spantable*/ count(*) from tb_route0",null));
		AssertKit.assertEqual(1, getCount(conn,"select/*spantable*/ count(*) from tb_route0",new Object[]{}));
		
		TableExistsMaintainer.lastMaintainResult = null;
		SQLTemplate.executeInsertSql(conn, "insert into tb_route0(f1,f2,f3) values('b',1,null)",null );		
		AssertKit.assertEqual(TableExistsMaintainer.lastMaintainResult.isSpecialCondition(),false);
		AssertKit.assertEqual(2, getCount(conn,"select/*spantable*/ count(*) from tb_route0",null));
		AssertKit.assertEqual(2,SQLTemplate.executeUpdateSql(conn, "update /*spantable*/ tb_route0 set f2=1", null));
		
		//验证修改
		DbCreateStringInt.drop();
		AssertKit.assertEqual(0,SQLTemplate.executeUpdateSql(conn, "update /*spantable*/ tb_route0 set f2=1", null));
		AssertKit.assertEqual(TableExistsMaintainer.lastMaintainResult.isReturnZeroRowUpdateStatement(),true);
		AssertKit.assertEqual(TableExistsMaintainer.lastMaintainResult.getTargetSqlForDummy(),null);

		//验证删除
		DbCreateStringInt.drop();
		AssertKit.assertEqual(0,SQLTemplate.executeDeleteSql(conn, "delete from  /*spantable*/ tb_route0", null));
		AssertKit.assertEqual(TableExistsMaintainer.lastMaintainResult.isReturnZeroRowUpdateStatement(),true);
		AssertKit.assertEqual(TableExistsMaintainer.lastMaintainResult.getTargetSqlForDummy(),null);
		
		//验证Hash Select 表不存在
		DbCreateStringInt.drop();
		AssertKit.assertEqual(0, getCount(conn,"select/*spantable*/ count(*) from tb_route0",null));
		MaintainReturn lmr = TableExistsMaintainer.lastMaintainResult;
		AssertKit.assertEqual(false,lmr.isReturnZeroRowUpdateStatement());
		AssertKit.assertEqual(TableExistsMaintainer.makeCountResult("`count(*)`"),lmr.getTargetSqlForDummy());
		AssertKit.assertException(()->SQLTemplate.executeSelect(connReal, "select * from tb_route0_1", null)); //表不存在
		AssertKit.assertException(()->SQLTemplate.executeSelect(connReal, "select * from tb_route0_2", null));
		
//		AssertKit.assertEqual(0, getCount(conn,"select /*spantable*/ * from tb_route0",null));
		DbCreateStringInt.drop();
		List<Map<String, String>> results = SQLTemplate.executeSelect(conn, "select /*spantable*/ count(*) from tb_route0",null);
		AssertKit.assertEqual(results.size(), 1);
		AssertKit.assertEqual(results.get(0).get("count(*)"), "0");
		
		results = SQLTemplate.executeSelect(conn, "select /*spantable*/ count(*) C from tb_route0",null);
		AssertKit.assertEqual(results.get(0).get("C"), "0");
		
		results = SQLTemplate.executeSelect(conn, "select * from (select /*spantable*/ count(*) C from tb_route0)",null);
		AssertKit.assertEqual(results.get(0).get("C"), "0");

		results = SQLTemplate.executeSelect(conn, "select * from (select /*spantable*/ count(*) C from tb_route0 group by f1)",null);
		AssertKit.assertEqual(results.size(),0);
		
		results = SQLTemplate.executeSelect(conn, "select * from (select /*spantable*/ count(*) C from tb_route0) group by f1",null);
		AssertKit.assertEqual(results.size(), 0);

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
