package net.jplugin.core.mtenant.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import net.jplugin.core.das.mybatis.impl.sess.MybatisSessionManager;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.mtenant.MtenantStatus;
/**
 * <li>如果当前不是多租户模式，则直接执行一次返回结果，租户号null
 * <li>如果是多租户模式，但是没有配置TenantListProvidor，则抛出异常
 * <li>如果是多租户模式，并且配置了TenantListProvidor，则逐个租户执行，返回结果列表；最后还原以前的租户ID状态。
 * @author LiuHang
 *
 */
public class TenantIteratorKit {
	
	/**
	 * 在所有本系统支持的的租户上迭代Runnable，返回结果列表
	 * @param r
	 * @return
	 */
	public static List<TenantResult> execute(Runnable r){
		return commonExecute(r,null);
	}
	/**
	 * 在所有本系统支持的的租户上迭代Callable，返回结果列表
	 * @param c
	 * @return
	 */
	public static List<TenantResult> execute(Callable c){
		return commonExecute(c,null);
	}
	/**
	 * 在指定的租户列表上迭代Runnable，返回结果列表
	 * @param r
	 * @param tenantsList
	 * @return
	 */
	public static List<TenantResult> execute(Runnable r,List<String> tenantsList){
		return commonExecute(r,tenantsList);
	}
	/**
	 * 在指定的租户列表上迭代Callable，返回结果列表
	 * @param c
	 * @param tenantsList
	 * @return
	 */
	public static List<TenantResult> execute(Callable c,List<String> tenantsList){
		return commonExecute(c,tenantsList);
	}
	
	private static List<TenantResult> commonExecute(Object runnableOrCallable,List<String> tenantsList){
		if (MtenantStatus.enabled()){
			if (TenantListProvidorManager.instance.isProviderExist()){
				
				List<String> list;
				if (tenantsList!=null){
					list = tenantsList;
				}else{
					list = TenantListProvidorManager.instance.getList();
				}
				
				List<TenantResult> results = new ArrayList(list.size());
				//save tenant id
				String oldTenantId = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
				for (String t:list){
					//no exception may be throw
					
					
					ThreadLocalContextManager.getRequestInfo().setCurrentTenantId(t);
					
					//2019-4-15避免动态数据源情况下，有缓存的Connection，增加下面一句。从云POS发现的问题。
					//以下<<
					MybatisSessionManager.releaseSessions();
					//>>以上
					
					results.add(runAndGetResult(t,runnableOrCallable));
				}
				//restore tenantid
				ThreadLocalContextManager.getRequestInfo().setCurrentTenantId(oldTenantId);
				
				//2019-4-15避免动态数据源情况下，有缓存的Connection，增加下面一句。从云POS发现的问题。
				//以下<<
				MybatisSessionManager.releaseSessions();
				//>>以上
				
				return results;
			}else{
				throw new RuntimeException("mtenant enabled and TenantListProvidor not configed, call TenantIteratorTemplate error");
			}
		}else{
			List<TenantResult> results= new ArrayList(1);
			results.add(runAndGetResult(null,runnableOrCallable));
			return results;
		}
	}
	
	private static TenantResult runAndGetResult(String tid,Object runnableOrCallable) {
		try{
			if (runnableOrCallable instanceof Runnable){
				
//				cleanMybasticsCacheIfNeeded();

				((Runnable)runnableOrCallable).run();
				return new TenantResult(tid,null,null);
			}else{
				Object r = ((Callable)runnableOrCallable).call();
				return new TenantResult(tid,r,null);
			}
		}catch(Throwable t){
			return new TenantResult(tid,null,t);
		}
	}
//
//	private static void cleanMybasticsCacheIfNeeded() {
//		if (net.jplugin.core.das.mybatis.Plugin.enabled){
//			Set<String> names = DataSourceFactory.getDataSourceNames();
//			for (String name:names){
//				IMybatisService svc = MyBatisServiceFactory.getService(name);
//				if (svc!=null){
//					svc.openSession().clearCache();
//				}
//			}
//		}
//	}
	
}
