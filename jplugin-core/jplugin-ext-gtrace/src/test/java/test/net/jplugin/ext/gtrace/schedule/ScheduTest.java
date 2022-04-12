package test.net.jplugin.ext.gtrace.schedule;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.jplugin.core.kernel.kits.ExecutorKit;

public class ScheduTest{
	
	public static ScheduTest instance ;
	
	public static void init(){
		instance = new ScheduTest();
	}
	
	ScheduledExecutorService svc = ExecutorKit.newScheduledThreadPool(5);

	public void test() throws Throwable {
//		svc.schedule(()->command(), 3, TimeUnit.SECONDS);
		svc.scheduleAtFixedRate(()->command(), 3,3, TimeUnit.SECONDS);
		svc.scheduleAtFixedRate(()->command2(), 3,3, TimeUnit.SECONDS);
	}
	
	long t = System.currentTimeMillis();
	public void command2(){
		long ttt = System.currentTimeMillis();
		System.out.println("running2."+Thread.currentThread().getName()+"  "+(ttt - t));
	}
	public void command(){
		long ttt = System.currentTimeMillis();
		System.out.println("running."+Thread.currentThread().getName()+"  "+(ttt - t));
		t = ttt;
//		try {
//			Thread.currentThread().sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}



}
