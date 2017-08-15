package net.jplugin.core.kernel.kits.scheduled;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScheduledExecutorServiceWrapper implements ScheduledExecutorService{
	ScheduledExecutorService inner ;

	public ScheduledExecutorServiceWrapper(ScheduledExecutorService o) {
		this.inner = o;
	}

	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return inner.schedule(new ScheduledRunnableWrapper(command), delay, unit);
	}

	@SuppressWarnings("unchecked")
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		return inner.schedule(new ScheduledCallableWrapper(callable), delay, unit);
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return inner.scheduleAtFixedRate(new ScheduledRunnableWrapper(command), initialDelay, period, unit);
	}

	public void shutdown() {
		inner.shutdown();
	}

	public List<Runnable> shutdownNow() {
		return inner.shutdownNow();
	}

	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		return inner.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}

	public boolean isShutdown() {
		return inner.isShutdown();
	}

	public boolean isTerminated() {
		return inner.isTerminated();
	}

	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return inner.awaitTermination(timeout, unit);
	}

	
	public void execute(Runnable command) {
		throw new RuntimeException("Not support");
	}
	
	public <T> Future<T> submit(Callable<T> task) {
		throw new RuntimeException("Not support");
	}

	public <T> Future<T> submit(Runnable task, T result) {
		throw new RuntimeException("Not support");
	}

	public Future<?> submit(Runnable task) {
		throw new RuntimeException("Not support");
	}

	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		throw new RuntimeException("Not support");
	}

	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		throw new RuntimeException("Not support");
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		throw new RuntimeException("Not support");
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		throw new RuntimeException("Not support");
	}
	
}
