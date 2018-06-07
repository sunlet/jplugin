package net.jplugin.core.ctx.impl.filter4clazz;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.filter.FilterManager;
import net.jplugin.common.kits.filter.IFilter;
import net.jplugin.core.ctx.api.AbstractRuleMethodInterceptor;
import net.jplugin.core.ctx.api.IRuleServiceFilter;
import net.jplugin.core.ctx.api.RuleServiceFilterContext;

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
		Object object = ctx.getObject();
		if (object!=null){
			//如果不是静态的，则查找实现类对应的class
			clazz = object.getClass();
		}else{
			//如果是静态的，则查找方法所爱的class
			clazz = ctx.getMethod().getClass();
		}
		
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
			Class<AbstractRuleMethodInterceptor> f = rcfd.getFilterClazz();
			AbstractRuleMethodInterceptor filter;
			try {
				filter = f.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("can't init object :"+f.getName());
			}
			filter.setFilterDefine(rcfd);
			fm.addFilter(filter);
		}
		
		//把后续的filter全部加入进来
		List<IFilter> filters = chain.getFollowingFilters();
		for (IFilter f:filters){
			fm.addFilter(f);
		}
		return fm;
	}

}
