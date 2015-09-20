package net.jplugin.core.das.hib.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.api.DataSourceHolder;
import net.jplugin.core.das.hib.api.IPersistObjDefinition;
import net.jplugin.core.das.hib.api.SinglePoDefine;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextListener;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.service.api.ServiceFactory;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-2-16 下午08:39:35
 **/

public class DASHelper {
	private static final String HIB_SESS_IN_CTX = "HIB_SESS_IN_CTX";
	private static final String HIB_TX_IN_CTX = "HIB_TX_IN_CTX";
	private SessionFactory sessionFactory;
//	private static ApplicationContext applicationContext = null;

	/**
	 * @param podefs 
	 * @param singlePoDefs 
	 * @param string
	 */
	public DASHelper(String factoryname, IPersistObjDefinition[] podefs, SinglePoDefine[] singlePoDefs) {
		//initApplicationContext();
//		sessionFactory = (SessionFactory) applicationContext
//				.getBean(factoryname);

//		Configuration cfg = new Configuration().addClass(null).configure();
		Configuration cfg = new Configuration();
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		cfg.setProperty("hibernate.show_sql", "true");
		cfg.setProperty("hibernate.cache.use_second_level_cache","false");
		cfg.setProperty("hibernate.hbm2ddl.auto",  "update");
		cfg.setProperty("hibernate.connection.provider_class",DBConnectionProvider.class.getName());
		
//		BasicDataSource ds = (BasicDataSource) applicationContext.getBean("dataSource");
		DataSource ds = DataSourceHolder.getInstance().getDataSource();	
		if (ds==null) {
			throw new RuntimeException("Can't find datasource:datasource");
		}
		DBConnectionProvider.ds.set(ds);
		
		addEntities(cfg,podefs,singlePoDefs);
		sessionFactory = cfg.buildSessionFactory();
		
		
		//因为das插件还没有启动好，所以只能获取数据源的连接，不能调用DataService方法
		Connection conn = null;
		try{
			conn = ds.getConnection();
			EntityIndexHelper.initIndexes(conn,podefs,singlePoDefs);
		}catch(SQLException se){
			throw new RuntimeException(se);
		}finally{
			if (conn!=null){
				try{conn.close();}catch(Exception e){}
			}
		}
	}

	

	/**
	 * @param cfg
	 * @param podefs
	 * @param singlePoDefs 
	 */
	private void addEntities(Configuration cfg, IPersistObjDefinition[] podefs,
			SinglePoDefine[] singlePoDefs) {

		//增加po集合的注册
		for (IPersistObjDefinition podef : podefs) {
			for (Class c : podef.getClasses()) {
				addPOClass(cfg,c);
			}
		}
		//增加单个po的注册
		for (SinglePoDefine s:singlePoDefs){
			addPOClass(cfg,s.getPoClass());
		}
	}

	/**
	 * @param c
	 */
	private void addPOClass(Configuration cfg,Class c) {
		String uri = ReflactKit.getShortName(c) + ".hbm.xml";

		if (c.getResource(uri)==null){
			cfg.addDocument(EntityXMLBuilderHelper.getHbmDom(c));
		}else{
			InputStream is = c.getResourceAsStream(uri);
			cfg.addInputStream(is);
			try {
				is.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}



//	/**
//	 * 
//	 */
//	private void initApplicationContext() {
//		if (applicationContext == null) {
//			synchronized (this.getClass()) {
//				if (applicationContext == null) {
//					String xml = PluginEnvirement.getInstance().getConfigDir()
//							+ "/spring-hib-das.xml";
//					applicationContext = new FileSystemXmlApplicationContext("file:"+xml);
//				}
//			}
//		}
//	}
	
	public Session getOrCreateSession(){
		ThreadLocalContext ctx = ThreadLocalContextManager.instance.getContext();
		Session sess = getSessionInCtx(ctx);
		if (sess != null){
			return sess;
		}
		sess = createSessionInCtx(ctx);
		
		//判断事物状态
		TransactionManager txm = ServiceFactory.getService(TransactionManager.class);
		if (txm.getStatus() != TransactionManager.Status.NOTX){
			Transaction tx = sess.beginTransaction();
			ctx.setAttribute(HIB_TX_IN_CTX, tx);
		}
		return sess;
	}
	
	public Session getSessionInCtx(ThreadLocalContext ctx) {
		return (Session) ctx.getAttribute(HIB_SESS_IN_CTX);
	}

	public Session createSessionInCtx(ThreadLocalContext ctx) {
		Session session = sessionFactory.openSession();
		ctx.setAttribute(HIB_SESS_IN_CTX, session);

		ctx.addContextListener(new ThreadLocalContextListener() {
			public void released(ThreadLocalContext rc) {
				Session thesess = (Session) rc.getAttribute(HIB_SESS_IN_CTX);
				if (thesess!=null){
					thesess.close();
					rc.setAttribute(HIB_SESS_IN_CTX, null);
				}
			}
		});
		return session;
	}



	public Transaction getTxInCtx(ThreadLocalContext ctx) {
		return (Transaction) ctx.getAttribute(HIB_TX_IN_CTX);
	}

//	public void setTxInCtx(ThreadLocalContext ctx,Transaction tx) {
//		 ctx.setAttribute(HIB_TX_IN_CTX,tx);
//	}

	/**
	 * @param tlc
	 */
	public void clearSessionAndTx(ThreadLocalContext tlc) {
		Session sess = (Session) tlc.getAttribute(HIB_SESS_IN_CTX);
		Transaction tx = (Transaction) tlc.getAttribute(HIB_TX_IN_CTX);
		if (tx!=null){
			tlc.setAttribute(HIB_TX_IN_CTX,null);
		}
		if (sess!=null){
			tlc.setAttribute(HIB_SESS_IN_CTX,null);
			sess.close();
		}
	}

}
