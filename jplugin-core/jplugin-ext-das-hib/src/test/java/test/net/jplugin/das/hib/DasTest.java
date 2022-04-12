package test.net.jplugin.das.hib;

import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.hib.api.IDataService;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-5 下午06:16:54
 **/

public class DasTest {
	public static void test() throws Exception{
		testNativeIdNoTx();
		testUUIDIDNoTx();
	}

	/**
	 * 没有事物情况下如果用UUID，不会插入；但是用native会插入，因为这样才能产生id
	 * @throws Exception
	 */
	public static void testUUIDIDNoTx() throws Exception{
		IDataService das = ServiceFactory.getService(IDataService.class);
//		TransactionManager txm = ServiceFactory.getService(TransactionManager.class);
		try{
//			txm.begin();
			das.executeDeleteSql("delete from DBPojoTest");
			String name = System.currentTimeMillis()+"";
			DBPojoTest o = new DBPojoTest();
			o.name = name;
			das.insert(o);
			System.out.println(o.getId());
			List<Object> result = das.queryBySql("select * from DBPojoTest");
			AssertKit.assertEqual(result.size(),0);
//			txm.commit();
		}catch(Exception e){
//			txm.rollback();
			throw e;
		}
	}
	
	public static void testNativeIdNoTx() throws Exception{
		IDataService das = ServiceFactory.getService(IDataService.class);
//		TransactionManager txm = ServiceFactory.getService(TransactionManager.class);
		try{
//			txm.begin();
			das.executeDeleteSql("delete from dbpojoidnativetest");
			
			String name = System.currentTimeMillis()+"";
			DBPojoIdNativeTest o = new DBPojoIdNativeTest();
			o.name = name;
			das.insert(o);
			System.out.println(o.getId());
			
			List<Object> result = das.queryBySql("select * from dbpojoidnativetest");
			AssertKit.assertEqual(result.size(),1);
//			txm.commit();
		}catch(Exception e){
//			txm.rollback();
			throw e;
		}
	}
		
}
