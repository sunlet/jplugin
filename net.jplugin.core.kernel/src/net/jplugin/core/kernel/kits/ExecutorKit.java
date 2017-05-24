package net.jplugin.core.kernel.kits;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

import java.util.concurrent.*;

/**
 * 鏀寔鑷姩缁存姢ThreadLocalContext鐨勭嚎绋嬫睜宸ュ叿绫�
 * 鏀寔鐨勫父鐢ㄦ柟娉曚笌java.util.concurrent.Executors鍏煎锛屾柟渚挎棤缂濆垏鎹�
 *
 * @author peiyu
 * @see Executors
 */
public final class ExecutorKit {

    /**
     * 鍒涘缓涓�涓寚瀹氬ぇ灏忕殑鏅�氱嚎绋嬫睜
     *
     * @param nThreads 绾跨▼鏁�
     * @return 绾跨▼姹�
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new TheadLocalContextExecutorService(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }

    /**
     * 鐢ㄦ寚瀹氱殑绾跨▼宸ュ巶鍒涘缓涓�涓寚瀹氬ぇ灏忕殑鏅�氱嚎绋嬫睜
     *
     * @param nThreads      绾跨▼鏁�
     * @param threadFactory 绾跨▼宸ュ巶
     * @return 绾跨▼姹�
     */
    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new TheadLocalContextExecutorService(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory);
    }

    /**
     * 鍒涘缓涓�涓彧鏈変竴涓嚎绋嬬殑绾跨▼姹�
     *
     * @return 绾跨▼姹�
     */
    public static ExecutorService newSingleThreadExecutor() {
        return new TheadLocalContextExecutorService(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }

    /**
     * 鐢ㄦ寚瀹氱殑绾跨▼宸ュ巶鍒涘缓涓�涓彧鏈変竴涓嚎绋嬬殑绾跨▼姹�
     *
     * @param threadFactory 绾跨▼宸ュ巶
     * @return 绾跨▼姹�
     */
    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new TheadLocalContextExecutorService(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory);
    }

    /**
     * 鍒涘缓涓�涓彲鏍规嵁闇�瑕佸垱寤烘柊绾跨▼鐨勬櫘閫氱嚎绋嬫睜
     *
     * @return 绾跨▼姹�
     */
    public static ExecutorService newCachedThreadPool() {
        return new TheadLocalContextExecutorService(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>());
    }

    /**
     * 鐢ㄦ寚瀹氱殑绾跨▼宸ュ巶鍒涘缓涓�涓彲鏍规嵁闇�瑕佸垱寤烘柊绾跨▼鐨勬櫘閫氱嚎绋嬫睜
     *
     * @param threadFactory 绾跨▼宸ュ巶
     * @return 绾跨▼姹�
     */
    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return new TheadLocalContextExecutorService(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                threadFactory);
    }

    /**
     * 鍒涘缓涓�涓崟绾跨▼鐨勭嚎绋嬫睜锛屽彲浠ユ墽琛屽欢鏃朵换鍔′互鍙婂畾鏈熻皟搴︾殑浠诲姟
     *
     * @return 绾跨▼姹�
     */
    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
        return new ScheduledTheadLocalContextExecutorService(1);
    }

    /**
     * 鐢ㄦ寚瀹氱殑绾跨▼宸ュ巶鍒涘缓涓�涓崟绾跨▼鐨勭嚎绋嬫睜锛屽彲浠ユ墽琛屽欢鏃朵换鍔′互鍙婂畾鏈熻皟搴︾殑浠诲姟
     *
     * @param threadFactory 绾跨▼宸ュ巶
     * @return 绾跨▼姹�
     */
    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
        return new ScheduledTheadLocalContextExecutorService(1, threadFactory);
    }

    /**
     * 鍒涘缓涓�涓寚瀹氱嚎绋嬫暟鐨勭嚎绋嬫睜锛屽彲浠ユ墽琛屽欢鏃朵换鍔′互鍙婂畾鏈熻皟搴︾殑浠诲姟
     *
     * @param corePoolSize 绾跨▼鏁�
     * @return 绾跨▼姹�
     */
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return new ScheduledTheadLocalContextExecutorService(corePoolSize);
    }

    /**
     * 鐢ㄦ寚瀹氱殑绾跨▼宸ュ巶鍒涘缓涓�涓寚瀹氱嚎绋嬫暟鐨勭嚎绋嬫睜锛屽彲浠ユ墽琛屽欢鏃朵换鍔′互鍙婂畾鏈熻皟搴︾殑浠诲姟
     *
     * @param corePoolSize  绾跨▼鏁�
     * @param threadFactory 绾跨▼宸ュ巶
     * @return 绾跨▼姹�
     */
    public static ScheduledExecutorService newScheduledThreadPool(
            int corePoolSize, ThreadFactory threadFactory) {
        return new ScheduledTheadLocalContextExecutorService(corePoolSize, threadFactory);
    }

    /**
     * ==========================================================================================================================================================
     **/

    /**
     * 鏀寔鑷姩缁存姢ThreadLocalContext鐨勬櫘閫氱嚎绋嬫睜
     */
    private static class TheadLocalContextExecutorService extends ThreadPoolExecutor {

        public TheadLocalContextExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public TheadLocalContextExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            ThreadLocalContextManager.instance.createContext();
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            ThreadLocalContextManager.instance.releaseContext();
        }
    }

    /**
     * 鏀寔鑷姩缁存姢ThreadLocalContext鐨勬敮鎸佸懆鏈熻皟搴︾殑绾跨▼姹�
     */
    private static class ScheduledTheadLocalContextExecutorService extends ScheduledThreadPoolExecutor {

        public ScheduledTheadLocalContextExecutorService(int corePoolSize) {
            super(corePoolSize);
        }

        public ScheduledTheadLocalContextExecutorService(int corePoolSize, ThreadFactory threadFactory) {
            super(corePoolSize, threadFactory);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            ThreadLocalContextManager.instance.createContext();
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            ThreadLocalContextManager.instance.releaseContext();
        }
    }

    private ExecutorKit() {
    }
}
