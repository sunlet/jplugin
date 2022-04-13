package net.luis.main;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;


import javax.servlet.ServletException;

import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.mock.HttpMock;
import net.jplugin.common.kits.http.mock.HttpServletRequestMock;
import net.jplugin.common.kits.http.mock.HttpServletResponseMock;
import net.jplugin.common.kits.http.mock.IHttpService;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.das.api.impl.ConnStaticsKit;
import net.jplugin.core.kernel.PluginApp;
import net.jplugin.core.kernel.api.PluginAutoDetect;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientFactory;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.webasic.impl.WebDriver;
import net.luis.main.event.ITestEventService;
import net.luis.main.pojo.IDataTest;
import net.luis.main.remote.IServerObject;
import net.luis.main.testrule.Rule1;
import net.luis.testautosearch.StartupAnnoTest;


/**
 * 
 * @author: sunlet
 * @version ����ʱ�䣺2015-3-8 ����12:39:43
 **/

public class Main {
	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws InterruptedException,
			SQLException, ClassNotFoundException {
		PluginAutoDetect.addAutoDetectPackage("net.luis");
		System.out.println("testAll="+System.getProperty("testAll")+" testTarget="+System.getProperty("testTarget"));
		PluginEnvirement.INSTANCE.setUnitTesting(true);
		HttpKit.setUnitTesting(true);
//		InitRequestInfoFilter.dummyAllowed = true;
		HttpMock.initServletSvc(new IHttpService() {
			public void dohttp(HttpServletRequestMock request,
					HttpServletResponseMock response) {
				try {
					WebDriver.INSTANCE.dohttp(request, response);
				} catch (ServletException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		PluginApp.main(args);
		
		StartupAnnoTest.assertCalled();
		
//		System.out.println(PluginEnvirement.getInstance().getPluginRegistry());
		
//		TestCall.test();
//		PluginEnvirement.getInstance().startup();
		PluginEnvirement.getInstance().stop();
		System.out.println("Connection count:"+ConnStaticsKit.INSTANCE.getConnectionCount());
		// testlog();
//		testrule1();
		// testSpringDas();
//		testdas();
//		testevent();
//		testremote();
//		testseriable();

	}

	/**
	 * 
	 */
	private static void testseriable() {
		int intv = 5;
		System.out.println(((Serializable)intv));
	}
private void test(){
	try{
		ThreadLocalContextManager.instance.createContext();
		//..ҵ�����
	}finally{
		ThreadLocalContextManager.instance.releaseContext();
	}
}
	/**
	 * 
	 */
	private static void testremote() {
		Client<IServerObject> client = ClientFactory.create(IServerObject.class, "http://localhost:8088/weapp/testremote2.do");
		IServerObject so = client.getObject();
		so.concact(new String[]{"a","b","c"});
		so.print();
		so.printString("hello");
		HashMap<String, String> p = new HashMap<String, String>();
		p.put("a", "aa");
		HashMap<String, String> ret = so.testHashMap(p);
		System.out.println(ret.get("a"));
		try{
			so.testex();
			throw new RuntimeException("error");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 
	 */
	private static void testevent() {
		PluginEnvirement.getInstance().startup();
		
		for (int i=0;i<100;i++){
			ITestEventService s = RuleServiceFactory.getRuleService(ITestEventService.class);
			s.test();
		}
		
	}

	/**
	 * 
	 */
	private static void testdas() {
		PluginEnvirement.getInstance().startup();
		
		IDataTest s = RuleServiceFactory.getRuleService(IDataTest.class);
		
//		s.testPropertyType();
		s.testTx(null);
	}

	/**
	 * @throws SQLException
	 * 
	 */
	private static void testSpringDas() throws SQLException {
//		PluginEnvirement.getInstance().startup();
//		ApplicationContext ac = new ClassPathXmlApplicationContext(
//				"config/spring-hib-das.xml");
//		DataSource datasource = (DataSource) ac.getBean("dataSource");
//
//		SessionFactory sessionFactory = (SessionFactory) ac
//				.getBean("sessionFactory");
//
//		HibernateTransactionManager transactionManager = (HibernateTransactionManager) ac
//				.getBean("transactionManager");
//		TransactionStatus tx = transactionManager
//				.getTransaction(new TransactionDefinition() {
//
//					public boolean isReadOnly() {
//						return false;
//					}
//
//					public int getTimeout() {
//						return 0;
//					}
//
//					public int getPropagationBehavior() {
//						return TransactionDefinition.PROPAGATION_REQUIRED;
//					}
//
//					public String getName() {
//						return "aaa";
//					}
//
//					public int getIsolationLevel() {
//						return TransactionDefinition.ISOLATION_DEFAULT;
//					}
//				});
//
//		Session sess = sessionFactory.getCurrentSession();
//		Connection conn = sess.connection();
//		System.out.println("autocommit = " + conn.getAutoCommit());
//
//		Session sess2 = sessionFactory.openSession();
//		System.out
//				.println("autocommit = " + sess2.connection().getAutoCommit());
//
//		Session sess3 = sessionFactory.openSession();
//
//		System.out
//				.println("autocommit = " + sess2.connection().getAutoCommit());
//
//		System.out.println(sess2.connection() == sess3.connection());
//
//		transactionManager.commit(tx);
//
//		// Transaction txx = sess.beginTransaction();
//		//		
//		// txx.commit();
//
//		// sess = sessionFactory.getCurrentSession();
//
//		// sess.beginTransaction();
//		sess = sessionFactory.getCurrentSession();
//		// sess = sessionFactory.openSession();
//		sess.beginTransaction();
//
//		//		
//		// Connection conn = datasource.getConnection();
//		//		
//		// System.out.println("auto commit = "+conn.getAutoCommit());
//		//		
//		// sess.beginTransaction();
//		//		
//		// System.out.println("auto commit = "+conn.getAutoCommit());
//		//		

	}

	/**
	 * 
	 */
	private static void testrule1() {
		PluginEnvirement.getInstance().startup();
		
		Rule1 r = RuleServiceFactory.getRuleService(Rule1.class);
		r.getUser(100);
		r.listUser("condition...");
		r.update();
		try {
			r.updateEx();
			throw new RuntimeException("shoudln't come here");
		} catch (Exception e) {
		}
		r.loopCallOk();
		try {
			r.loopCallError();
			throw new RuntimeException("shoudln't come here");
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 */
	private static void testlog() {
		PluginEnvirement.getInstance().startup();

		ILogService logsvc = ServiceFactory.getService(ILogService.class);
		Logger logger = logsvc.getLogger(Main.class.getName());
		logger.info("main start ok");

		Logger lll = logsvc.getSpecicalLogger("ttt");
		for (int i = 0; i < 3; i++)
			lll.info("-log the data.................");

	}

}
