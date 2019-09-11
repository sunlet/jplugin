package net.jplugin.core.das.api.impl;

import java.util.concurrent.atomic.AtomicInteger;

public class ConnStaticsKit {
	public static ConnStaticsKit INSTANCE = new ConnStaticsKit();
	AtomicInteger count = new AtomicInteger();
	
	
	public void logGetConn() {
		count.incrementAndGet();
	}
	
	public void logCloseConn() {
		count.decrementAndGet();
	}
	
	public int getConnectionCount() {
		return count.get();
	}
}
