package net.jplugin.core.kernel.api.ctx;



/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-11 上午09:40:56
 **/

public class ThreadLocalContextManager {
	ThreadLocal<ThreadLocalContext> ctxLocal=new ThreadLocal<ThreadLocalContext>();
	
	public static ThreadLocalContextManager instance = new ThreadLocalContextManager();
	
	public static RequesterInfo getRequestInfo(){
		return ThreadLocalContextManager.instance.getContext().getRequesterInfo();
	}
	
	public static ThreadLocalContext getCurrentContext(){
		return instance.getContext();
	}
	
//	public static ThreadLocalContext currentContet(){
//		return ThreadLocalContextManager.instance.getContext();
//	}

	
	public ThreadLocalContext getContext(){
		ThreadLocalContext ctx = ctxLocal.get();
		return ctx;
	}
	/**
	 * 如果是http调用，则在http拦截器中产生/销毁ctx
	 * 如果是异步调用，在调用处理器中产生/销毁ctx
	 * 确保不会出现不匹配！！！
	 * Context只保留一个，不采用堆栈机制，保持简单。
	 * @param rc
	 * @return 
	 */
	public ThreadLocalContext createContext(){
		if (ctxLocal.get()!=null){
			throw new RuntimeException("Ctx state not right!");
		}
		ThreadLocalContext rc = new ThreadLocalContext();
		ctxLocal.set(rc);
		
		return rc;
	}
	
	public boolean createContextIfNotExists() {
		if (ctxLocal.get()==null) {
			createContext();
			return true;
		}
		return false;
	}
//	public static void releaseCurrentContext(){
//		instance.releaseContext();
//	}
	
	public void releaseContext(){
		ThreadLocalContext ctx = ctxLocal.get();
		if (ctx==null){
			throw new RuntimeException("Can't find ctx!");
		}
		ctxLocal.set(null);
		ctx.release();
	}

	public static void runInContext(Runnable r) {
		boolean b = false;
		try{
			b = ThreadLocalContextManager.instance.createContextIfNotExists();
			r.run();
		}finally {
			if (b)
				ThreadLocalContextManager.instance.releaseContext();
		}
	}
}
