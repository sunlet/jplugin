package net.jplugin.core.event.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.jplugin.core.event.api.Event;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-9 下午03:07:24
 **/

public class PostMemoryThreadPoolExecutor {
	public static PostMemoryThreadPoolExecutor instance = new PostMemoryThreadPoolExecutor();
	
	ThreadPoolExecutor executor;

	private long keepAliveTime = 60;

	private int corePoolSize = 0;

	private int maxPoolSize = 5;

	private BlockingQueue<Runnable> workQueue;
	
	boolean init;
	public void init(){
		if (!init){
			synchronized (this) {
				if (!init){
					init = true;
					workQueue = new LinkedBlockingQueue<Runnable>();
					executor = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime,TimeUnit.SECONDS,workQueue);
				}
			}
		}
		
	}

	/**
	 * @param e
	 */
	public void execute(Runnable r) {
		init();
		executor.execute(r);
	}
	
}
