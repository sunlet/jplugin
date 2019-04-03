package net.jplugin.core.ctx.impl.filter4clazz;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.filter.FilterManager;
import net.jplugin.common.kits.filter.IFilter;
import net.jplugin.core.ctx.api.AbstractRuleMethodInterceptor;
import net.jplugin.core.ctx.api.IRuleServiceFilter;
import net.jplugin.core.ctx.api.RuleServiceFilterContext;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 * <PRE>
 * 这个类本身是一个IRuleServiceFilter，但是它比较特殊。
 * 它在Filter时候，假设Filter的next为 nextA.
 * 1.会根据当前的Object所在的类找一个FilterManager（以类区分的FilterManager，标记为CFM）.
 *   如果找不到就创建一个新的(标记为CFM)。并把当前新创建的FilterManage的最后一个，设置为nextA。
 * 2.然后调用CMF的next。
 * 
 * 这里的FilterManager是在对应的类第一次使用的时候初始化的。
 * </PRE>
 * @author LiuHang
 *
 */
public class RuleCallFilterManagerRuleFilter implements IRuleServiceFilter{

	private static final Object EMPTY_OBJECT = new Object(){
		public String toString() {
			return "EMPTY_OBJECT";
		};
	};
	ConcurrentHashMap<Class, Object> filterManagers=new ConcurrentHashMap<>();
	
	@Override
	public Object filter(FilterChain fc, RuleServiceFilterContext ctx) throws Throwable {
		Class clazz;
//		Object object = ctx.getObject();
//		if (object!=null){
//			//如果不是静态的，则查找实现类对应的class
//			clazz = object.getClass();
//		}else{
//			//如果是静态的，则查找方法所爱的class
//			clazz = ctx.getMethod().getClass();
//		}
		clazz = ctx.getMethod().getDeclaringClass();//直接获取Method所在的class，而这个Method正好又是有Rule的Method，所以一切就一致起来了：）
		
		Object fm = findFilterManager(clazz,fc);
		/**
		 * <PRE>
		 * 如果得到null，表示没有对应的过滤，按照正常处理
		 * 否则调用对应的filterManager
		 * </PRE>
		 */
		if (fm==EMPTY_OBJECT){
			return fc.next(ctx);
		}else{
			return ( (FilterManager<RuleServiceFilterContext> )fm).filter(ctx);
		}
	}

	/**
	 * 返回的可能是空对象EMTPY_OBJECT,或者FilterManager对象
	 * @param clazz
	 * @param chain
	 * @return
	 */
	private Object findFilterManager(Class clazz, FilterChain chain) {
		Object ret = filterManagers.get(clazz);
		if (ret != null){
			return ret;
		}else{
			synchronized (filterManagers) {
				ret = filterManagers.get(clazz);
				if (ret == null){
					FilterManager<RuleServiceFilterContext> fm = tryCreateFilterManager(clazz,chain);
					if (fm!=null){
						filterManagers.put(clazz, fm);
					}else{
						//因为 ConcurrentHashMap value不能为null，所以有时放入 ""
						filterManagers.put(clazz, EMPTY_OBJECT);
					}
				}
			}
			return filterManagers.get(clazz);
		}
		
	}

	private FilterManager<RuleServiceFilterContext> tryCreateFilterManager(Class clazz, FilterChain chain) {
		//查找本类
		List<RuleCallFilterDefine> list = RuleCallFilterDefineManager.INSTANCE.getMatchedDefinesForClass(clazz);
		if (list.isEmpty() || list==null)
			return null;
		
		FilterManager fm  = new FilterManager<>();
		for (RuleCallFilterDefine rcfd:list){
			
			ClassOwnedFilter filter = createClassOwnedFilter(rcfd);
			fm.addFilter(filter);
		}
		
		//把后续的filter全部加入进来
		List<IFilter> filters = chain.getFollowingFilters();
		for (IFilter f:filters){
			fm.addFilter(f);
		}
		return fm;
	}

	HashMap<Class,AbstractRuleMethodInterceptor> inceptInstanceMap=new HashMap<>();
	
	/**
	 * 只在对应的Rule类第一次调用的时候创建，除了第一次初始化inceptor其他都很快，所以sync没大关系。
	 * @param filterDefine
	 * @return
	 */
	
	private synchronized ClassOwnedFilter createClassOwnedFilter(RuleCallFilterDefine filterDefine) {
//		ClassOwnedFilter filter = 
		
		Class<AbstractRuleMethodInterceptor> filterClazz = filterDefine.getFilterClazz();
		
		//从缓存获取，获取不到时重新创建一个，保证一个inceptor只有一个实例！
		AbstractRuleMethodInterceptor inteceptor = inceptInstanceMap.get(filterClazz);
		if (inteceptor == null){
			try{
				inteceptor = filterClazz.newInstance();
				PluginEnvirement.INSTANCE.resolveRefAnnotation(inteceptor);
				inceptInstanceMap.put(filterClazz, inteceptor);
			}catch(Exception e){
				throw new RuntimeException("can't init object :"+filterClazz.getName());
			}
		}
		return new ClassOwnedFilter(filterDefine,inteceptor);
		
//		Class<AbstractRuleMethodInterceptor> f = rcfd.getFilterClazz();
//		AbstractRuleMethodInterceptor filter;
//		try {
//			filter = f.newInstance();
//			PluginEnvirement.INSTANCE.resolveRefAnnotation(filter);
//		} catch (Exception e) {
//			throw new RuntimeException("can't init object :"+f.getName());
//		}
//		filter.setFilterDefine(rcfd);

	}
	
	
	static class ClassOwnedFilter implements IFilter<RuleServiceFilterContext>{

		private RuleCallFilterDefine filterDefine;
		private AbstractRuleMethodInterceptor ruleInterceptor;

		public ClassOwnedFilter(RuleCallFilterDefine fd,AbstractRuleMethodInterceptor inceptor) {
			this.filterDefine = fd;
			this.ruleInterceptor = inceptor;
		}

		@Override
		public Object filter(FilterChain fc, RuleServiceFilterContext ctx) throws Throwable {
			//委托给真正的拦截器执行
			return ruleInterceptor.filter(fc, ctx, filterDefine);
		}
	}
}
