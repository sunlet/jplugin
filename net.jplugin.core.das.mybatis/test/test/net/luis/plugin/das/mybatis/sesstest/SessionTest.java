package test.net.luis.plugin.das.mybatis.sesstest;

import org.apache.ibatis.session.SqlSession;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.mybatis.impl.IMybatisService;
import net.jplugin.core.service.api.ServiceFactory;

public class SessionTest {

	public void test(){
		IMybatisService svc = ServiceFactory.getService(IMybatisService.class);
		TransactionManager txm = ServiceFactory.getService(TransactionManager.class);
		
		SqlSession s1,s2 ;
		s1 = svc.openSession();
		s2 = svc.openSession();
		
		AssertKit.assertEqual(s1, s2);
		txm.begin();
		
		s1 = svc.openSession();
		AssertKit.assertFalse(s1==s2);
		s2 = svc.openSession();
		AssertKit.assertEqual(s1, s2);
		
		txm.commit();
		
		s1 = svc.openSession();
		AssertKit.assertFalse(s1==s2);
		s2 = svc.openSession();
		AssertKit.assertEqual(s1, s2);
		
		txm.rollback();
		s1 = svc.openSession();
		AssertKit.assertFalse(s1==s2);
	}
}
