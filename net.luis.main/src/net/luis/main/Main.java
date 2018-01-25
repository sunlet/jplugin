package net.luis.main;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;

import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.common.kits.http.mock.HttpMock;
import net.jplugin.common.kits.http.mock.HttpServletRequestMock;
import net.jplugin.common.kits.http.mock.HttpServletResponseMock;
import net.jplugin.common.kits.http.mock.IHttpService;
import net.jplugin.core.config.api.RefConfig;
import net.jplugin.core.ctx.api.RefRuleService;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.mybatis.api.RefMybatisService;
import net.jplugin.core.kernel.PluginApp;
import net.jplugin.core.kernel.api.PluginAutoDetect;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.kernel.kits.ExecutorKit;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.mtenant.api.TenantIteratorKit;
import net.jplugin.core.mtenant.api.TenantIteratorRunnableAdaptor;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientFactory;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.webasic.api.Para;
import net.jplugin.ext.webasic.impl.InitRequestInfoFilter;
import net.jplugin.ext.webasic.impl.WebDriver;
import net.luis.main.event.ITestEventService;
import net.luis.main.pojo.IDataTest;
import net.luis.main.remote.IServerObject;
import net.luis.main.testrule.Rule1;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-3-8 上午12:39:43
 **/

public class Main {

	public static void main1(String[] arg) throws IOException, HttpStatusException{
		for (int i=0;i<3;i++){
			System.out.println("times:"+i);
			long time = System.currentTimeMillis();
			try{
				String ret = HttpKit.get("http://192.168.165.11/32234");
//				String ret = HttpKit.get("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=0&rsv_idx=1&tn=baidu&wd=asdf&rsv_pq=f689970c000219c6&rsv_t=40c7p%2BGoWt3L%2FmeJUra6M6XP3qWRn1s6yYy4e07mBa1X8yyrQI3biq2D8OY&rsv_enter=0&rsv_sug3=5&rsv_sug1=2&rsv_sug7=100&inputT=1267&rsv_sug4=1375");
				System.out.println("return "+ret+" dural="+(System.currentTimeMillis()-time));
			}catch(org.apache.http.conn.HttpHostConnectException e){
				System.out.println("HttpHostConnectionException:"+e.getMessage()+"  dural="+ (System.currentTimeMillis() - time));
			}catch(org.apache.http.conn.ConnectTimeoutException e){
				System.out.println("org.apache.http.conn.ConnectTimeoutException:"+e.getMessage()+"  dural="+ (System.currentTimeMillis() - time));
			}
		}
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws InterruptedException,
			SQLException {
		PluginAutoDetect.addAutoDetectPackage("net");
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
//		PluginEnvirement.getInstance().startup();
		PluginEnvirement.getInstance().stop();
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
		//..业务代码
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
