package net.jplugin.core.kernel.kits;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.jplugin.core.kernel.kits.scheduled.ScheduledExecutorServiceWrapper;

/**
 * 支持自动维护ThreadLocalContext的线程池工具类
 * 支持的常用方法与java.util.concurrent.Executors兼容，方便无缝切换
 *
 * @author peiyu
 * @see Executors
 */
public final class ExecutorKit {
	
	private static int maxQueueSize = 100000;
	private static int maxCachedThreadNum = 2000;
	
    /**
     * 创建一个指定大小的普通线程池
     *
     * @param nThreads 线程数
     * @return 线程池
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new TheadLocalContextExecutorService(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(maxQueueSize));
    }

    /**
     * 用指定的线程工厂创建一个指定大小的普通线程池
     *
     * @param nThreads      线程数
     * @param threadFactory 线程工厂
     * @return 线程池
     */
    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new TheadLocalContextExecutorService(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(maxQueueSize),
                threadFactory);
    }
    
    /**
     * 用指定的线程工厂创建一个指定大小的普通线程池
     *
     * @param nThreads      线程数
     * @param threadFactory 线程工厂
     * @return 线程池
     */
    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory,int queueSize) {
        return new TheadLocalContextExecutorService(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize),
                threadFactory);
    }

    /**
     * 创建一个只有一个线程的线程池
     *
     * @return 线程池
     */
    public static ExecutorService newSingleThreadExecutor() {
        return new TheadLocalContextExecutorService(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(maxQueueSize));
    }
    


    /**
     * 用指定的线程工厂创建一个只有一个线程的线程池
     *
     * @param threadFactory 线程工厂
     * @return 线程池
     */
    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new TheadLocalContextExecutorService(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(maxQueueSize),
                threadFactory);
    }
    
    /**
     * 用指定的线程工厂创建一个只有一个线程的线程池
     *
     * @param threadFactory 线程工厂
     * @return 线程池
     */
    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory,int queueSize) {
        return new TheadLocalContextExecutorService(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize),
                threadFactory);
    }

    /**
     * 创建一个可根据需要创建新线程的普通线程池
     *
     * @return 线程池
     */
    public static ExecutorService newCachedThreadPool() {
        return new TheadLocalContextExecutorService(0, maxCachedThreadNum ,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>());
    }

    /**
     * 用指定的线程工厂创建一个可根据需要创建新线程的普通线程池
     *
     * @param threadFactory 线程工厂
     * @return 线程池
     */
    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return new TheadLocalContextExecutorService(0, maxCachedThreadNum,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                threadFactory);
    }

    /**
     * 用指定的线程工厂创建一个可根据需要创建新线程的普通线程池
     *
     * @param threadFactory 线程工厂
     * @return 线程池
     */
    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory,int maxThreadNum) {
        return new TheadLocalContextExecutorService(0, maxThreadNum,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                threadFactory);
    }
    
    /**
     * 创建一个单线程的线程池，可以执行延时任务以及定期调度的任务
     *
     * @return 线程池
     */
    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
    	return new ScheduledExecutorServiceWrapper(Executors.newSingleThreadScheduledExecutor());
//        return new ScheduledTheadLocalContextExecutorService(1);
    }

    /**
     * 用指定的线程工厂创建一个单线程的线程池，可以执行延时任务以及定期调度的任务
     *
     * @param threadFactory 线程工厂
     * @return 线程池
     */
    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
    	return new ScheduledExecutorServiceWrapper(Executors.newSingleThreadScheduledExecutor(threadFactory));
//        return new ScheduledTheadLocalContextExecutorService(1, threadFactory);
    }

    /**
     * 创建一个指定线程数的线程池，可以执行延时任务以及定期调度的任务
     *
     * @param corePoolSize 线程数
     * @return 线程池
     */
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
    	return new ScheduledExecutorServiceWrapper(Executors.newScheduledThreadPool(corePoolSize));
//        return new ScheduledTheadLocalContextExecutorService(corePoolSize);
    }

    /**
     * 用指定的线程工厂创建一个指定线程数的线程池，可以执行延时任务以及定期调度的任务
     *
     * @param corePoolSize  线程数
     * @param threadFactory 线程工厂
     * @return 线程池
     */
    public static ScheduledExecutorService newScheduledThreadPool(
            int corePoolSize, ThreadFactory threadFactory) {
    	return new ScheduledExecutorServiceWrapper(Executors.newScheduledThreadPool(corePoolSize,threadFactory));
//        return new ScheduledTheadLocalContextExecutorService(corePoolSize, threadFactory);
    }

    /**
     * ==========================================================================================================================================================
     **/

    /**
     * 支持自动维护ThreadLocalContext的普通线程池
     */
    private static class TheadLocalContextExecutorService extends ThreadPoolExecutor {

        public TheadLocalContextExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public TheadLocalContextExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }
        
        @Override
        public void execute(Runnable command) {
        	super.execute(new RunnableWrapper(command));
        }

//        @Override
//        protected void beforeExecute(Thread t, Runnable r) {
//            ThreadLocalContextManager.instance.createContext();
//        }
//
//        @Override
//        protected void afterExecute(Runnable r, Throwable t) {
//            ThreadLocalContextManager.instance.releaseContext();
//        }
    }
 

//    /**
//     * 支持自动维护ThreadLocalContext的支持周期调度的线程池
//     */
//    private static class ScheduledTheadLocalContextExecutorService extends ScheduledThreadPoolExecutor {
//
//        public ScheduledTheadLocalContextExecutorService(int corePoolSize) {
//            super(corePoolSize, new ThreadFactoryWrapper(Executors.defaultThreadFactory()));
//        }
//
//        public ScheduledTheadLocalContextExecutorService(int corePoolSize, ThreadFactory threadFactory) {
//            super(corePoolSize, new ThreadFactoryWrapper(threadFactory));
//        }
//
//        @Override
//        public void execute(Runnable command) {
//            super.execute(new RunnableWrapper(command));
//        }
//
//        static class ThreadFactoryWrapper implements ThreadFactory {
//
//            ThreadFactory inner;
//
//            public ThreadFactoryWrapper(ThreadFactory threadFactory) {
//                this.inner = threadFactory;
//            }
//
//            @Override
//            public Thread newThread(Runnable r) {
//                return inner.newThread(new RunnableWrapper(r));
//            }
//        }
//
////        @Override
////        protected void beforeExecute(Thread t, Runnable r) {
////            ThreadLocalContextManager.instance.createContext();
////        }
////
////        @Override
////        protected void afterExecute(Runnable r, Throwable t) {
////            ThreadLocalContextManager.instance.releaseContext();
////        }
//    }
    

    private ExecutorKit() {
    }
}
