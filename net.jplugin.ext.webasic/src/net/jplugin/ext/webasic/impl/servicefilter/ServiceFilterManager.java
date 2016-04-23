package net.jplugin.ext.webasic.impl.servicefilter;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.webasic.api.IServiceFilter;
import net.jplugin.ext.webasic.api.ServiceFilterContext;
import net.jplugin.ext.webasic.Plugin;

public class ServiceFilterManager {
	static List<IServiceFilter> filters;
	
	public static void init(){
		IServiceFilter[] arr = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_SERVICEFILTER,IServiceFilter.class);
		filters = new ArrayList<IServiceFilter>(arr.length);
		for (int i=0;i<arr.length;i++){
			filters.add(arr[i]);
		}
	}
	
	public static boolean hasFilter(){
		return filters!=null && !filters.isEmpty();
	}
	
	public static Object executeWithFilter(ServiceFilterContext ctx,IServiceCallback r) throws Throwable{
		if (filters==null) return r.run();
		else{
			//先正序执行
			for (IServiceFilter f:filters){
				if (!f.before(ctx)) 
					throw new ServiceIllegleAccessException(ctx);
			}
			try{
				ctx.setResult(r.run());
			}catch(Throwable t){
				ctx.setTh(t);
			}

			//最后倒序执行
			for (int i=filters.size()-1;i>=0;i--){
				IServiceFilter f = filters.get(i);
				try{
					f.after(ctx);
				}catch(Exception e){
					e.printStackTrace();
					ServiceFactory.getService(ILogService.class).getLogger(ServiceFilterManager.class.getName()).error("Error when do service after filter", e);
				}
			}
			
			if (ctx.getTh()==null) return ctx.getResult();
			else throw ctx.getTh();
		}
	}

	
}
