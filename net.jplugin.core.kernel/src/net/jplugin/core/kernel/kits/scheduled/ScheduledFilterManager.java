package net.jplugin.core.kernel.kits.scheduled;

import java.util.concurrent.Callable;

import net.jplugin.core.kernel.api.PluginFilterManager;

public class ScheduledFilterManager {
	static PluginFilterManager<Object> filter=new PluginFilterManager<Object>(net.jplugin.core.kernel.Plugin.EP_EXE_SCHEDULED_FILTER, (fc,ctx)->{
		if (ctx instanceof Runnable){
			((Runnable)ctx).run();
			return null;
		}else{
			return ((Callable)ctx).call();
		}
	}); 
	public static void init(){
		filter.init();
	}
}
