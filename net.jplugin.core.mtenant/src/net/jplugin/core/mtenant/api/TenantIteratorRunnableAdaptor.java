package net.jplugin.core.mtenant.api;

public class TenantIteratorRunnableAdaptor implements Runnable{
	Runnable runnable;
	public TenantIteratorRunnableAdaptor(Runnable r){
		runnable = r;
	}
	
	@Override
	public void run() {
		TenantIteratorKit.execute(runnable);
	}
	
}
