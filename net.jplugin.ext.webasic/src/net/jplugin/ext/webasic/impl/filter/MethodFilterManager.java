package net.jplugin.ext.webasic.impl.filter;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.webasic.api.IInvocationFilter;
import net.jplugin.ext.webasic.api.InvocationContext;
import net.jplugin.ext.webasic.Plugin;

public abstract class MethodFilterManager {
	protected List<IInvocationFilter> filters;
	
	public abstract void init();
	
	public boolean hasFilter(){
		return filters!=null && !filters.isEmpty();
	}
	
	public Object executeWithFilter(InvocationContext ctx,IMethodCallback r) throws Throwable{
		if (filters==null) return r.run();
		else{
			//先正序执行
			for (IInvocationFilter f:filters){
				if (!f.before(ctx)) 
					throw new MethodIllegleAccessException(ctx);
			}
			try{
				ctx.setResult(r.run());
			}catch(Throwable t){
				ctx.setTh(t);
			}

			//最后倒序执行
			for (int i=filters.size()-1;i>=0;i--){
				IInvocationFilter f = filters.get(i);
				try{
					f.after(ctx);
				}catch(Exception e){
					e.printStackTrace();
					ServiceFactory.getService(ILogService.class).getLogger(MethodFilterManager.class.getName()).error("Error when do service after filter", e);
				}
			}
			
			if (ctx.getTh()==null) return ctx.getResult();
			else throw ctx.getTh();
		}
	}

	
}
