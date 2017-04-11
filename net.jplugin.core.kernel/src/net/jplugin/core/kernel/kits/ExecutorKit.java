package net.jplugin.core.kernel.kits;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

import java.util.concurrent.*;

/**
 * 支持自动维护ThreadLocalContext的线程池工具类
 * 支持的常用方法与java.util.concurrent.Executors兼容，方便无缝切换
 *
 * @author peiyu
 * @see Executors
 */
public final class ExecutorKit {

    /**
     * 创建一个指定大小的普通线程池
     *
     * @param nThreads 线程数
     * @return 线程池
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new TheadLocalContextExecutorService(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
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
                new LinkedBlockingQueue<>(),
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
                new LinkedBlockingQueue<>());
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
                new LinkedBlockingQueue<>(),
                threadFactory);
    }

    /**
     * 创建一个可根据需要创建新线程的普通线程池
     *
     * @return 线程池
     */
    public static ExecutorService newCachedThreadPool() {
        return new TheadLocalContextExecutorService(0, Integer.MAX_VALUE,
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
        return new TheadLocalContextExecutorService(0, Integer.MAX_VALUE,
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
        return new ScheduledTheadLocalContextExecutorService(1);
    }

    /**
     * 用指定的线程工厂创建一个单线程的线程池，可以执行延时任务以及定期调度的任务
     *
     * @param threadFactory 线程工厂
     * @return 线程池
     */
    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
        return new ScheduledTheadLocalContextExecutorService(1, threadFactory);
    }

    /**
     * 创建一个指定线程数的线程池，可以执行延时任务以及定期调度的任务
     *
     * @param corePoolSize 线程数
     * @return 线程池
     */
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return new ScheduledTheadLocalContextExecutorService(corePoolSize);
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
        return new ScheduledTheadLocalContextExecutorService(corePoolSize, threadFactory);
    }

    /**
     * ==========================================================================================================================================================
     **/

    /**
     * 普通线程池
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
            super.execute(new TheadLocalContextRunner(command));
        }
    }

    /**
     * 支持周期调度的线程池
     */
    private static class ScheduledTheadLocalContextExecutorService extends ScheduledThreadPoolExecutor {

        public ScheduledTheadLocalContextExecutorService(int corePoolSize) {
            super(corePoolSize);
        }

        public ScheduledTheadLocalContextExecutorService(int corePoolSize, ThreadFactory threadFactory) {
            super(corePoolSize, threadFactory);
        }

        @Override
        public void execute(Runnable command) {
            super.execute(new TheadLocalContextRunner(command));
        }
    }

    /**
     * 执行器包装类，实现自动维护ThreadLocalContext
     */
    private static class TheadLocalContextRunner implements Runnable {

        private final Runnable runnable;

        public TheadLocalContextRunner(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            try {
                ThreadLocalContextManager.instance.createContext();
                this.runnable.run();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            } finally {
                ThreadLocalContextManager.instance.releaseContext();
            }
        }
    }

    private ExecutorKit() {
    }
}
