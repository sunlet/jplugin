package net.jplugin.core.das.mybatis.impl.sess;

import java.util.Hashtable;

import org.apache.ibatis.session.SqlSession;

import net.jplugin.core.das.mybatis.api.MyBatisServiceFactory;
import net.jplugin.core.das.mybatis.impl.MybaticsServiceImplNew;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextListener;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.service.api.ServiceFactory;

public class MybatisSessionManager {
	
	private static final String MYBATIS_SESSIONS = "MYBATIS_SESSIONS";
	private static ThreadLocalContextListener sessionClearCtxListener = new SessionClearContextListener();

	public static SqlSession getSession(String dataSourceName){
		Hashtable<String, SqlSessionAdaptor> sessions = getSessions();
		SqlSessionAdaptor sess = sessions.get(dataSourceName);
		if (sess==null){
			//创建session并放入
			MybaticsServiceImplNew service = (MybaticsServiceImplNew) MyBatisServiceFactory.getService(dataSourceName);
			SqlSession sessInner = service._openRealSession();
			sess = new SqlSessionAdaptor(sessInner);
			//尝试注册context监听器
			ThreadLocalContextManager.instance.getContext().addContextListenerOnce(sessionClearCtxListener );
			//放入Session Map
			sessions.put(dataSourceName, sess);
		}
		return sess;
	}

	private static Hashtable<String, SqlSessionAdaptor> getSessions(){
		Hashtable<String, SqlSessionAdaptor> sessions = (Hashtable<String, SqlSessionAdaptor>) ThreadLocalContextManager.instance.getContext().getAttribute(MYBATIS_SESSIONS);
		if (sessions == null){
			sessions = new Hashtable<>();
			ThreadLocalContextManager.instance.getContext().setAttribute(MYBATIS_SESSIONS, sessions);
		}
		return sessions;
	}


	
	public static void clearSessions(){
		Hashtable<String, SqlSessionAdaptor> sessions = getSessions();
		for (SqlSessionAdaptor s:sessions.values()){
			s.getRealSession().clearCache();
		}
	}
	
	public static void releaseSessions(){
		Hashtable<String, SqlSessionAdaptor> sessions = getSessions();
		if (sessions!=null){
			for (SqlSessionAdaptor s:sessions.values()){
				s.getRealSession().close();
			}
			//清空hashmap
			sessions.clear();
		}
		
	}

	public static void releaseSessions(ThreadLocalContext rc) {
		Hashtable<String, SqlSessionAdaptor> sessions = (Hashtable<String, SqlSessionAdaptor>) rc.getAttribute(MYBATIS_SESSIONS);
		if (sessions!=null){
			for (SqlSessionAdaptor s:sessions.values()){
				s.getRealSession().close();
			}
			//清空hashmap
			sessions.clear();
		}
	}
}
