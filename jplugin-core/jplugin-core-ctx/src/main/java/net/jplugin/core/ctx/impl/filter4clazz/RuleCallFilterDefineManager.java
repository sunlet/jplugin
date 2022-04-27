package net.jplugin.core.ctx.impl.filter4clazz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.jplugin.common.kits.SortUtil;
import net.jplugin.core.ctx.api.AbstractRuleMethodInterceptor;
import net.jplugin.core.kernel.api.ExtensionObjects;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class RuleCallFilterDefineManager {
	
	public static RuleCallFilterDefineManager INSTANCE = new RuleCallFilterDefineManager();

	RuleCallFilterDefineBean[] defSetList;
	List<RuleCallFilterDefine> defList;

	public void initialize() {
		
		HashMap<Class,AbstractRuleMethodInterceptor> filterInstanceMap = new HashMap();
		
		defSetList = PluginEnvirement.getInstance().getExtensionObjects(net.jplugin.core.ctx.Plugin.EP_RULE_METHOD_INTERCEPTOR,RuleCallFilterDefineBean.class);
		defList = new ArrayList();
		for (RuleCallFilterDefineBean ds : defSetList) {
			//get filter instance
			AbstractRuleMethodInterceptor instance = getOrCreateFilterInstance(ds.getFilterClass(),filterInstanceMap);
			ExtensionObjects.resetValue(ds, instance);
			//get filter instance ok
			
			List<RuleCallFilterDefine> list ;
			try{
//				list = RuleCallFilterDefine.parse(filterProperty(ds.getApplyTo()));
				list = RuleCallFilterDefine.parse(ds.getApplyTo());
			}catch(Exception e){
				throw new RuntimeException("Binding annotation error, applyto= "+ds.getApplyTo()+" filter="+ds.getFilterClass().getName(),e);
			}
			for (RuleCallFilterDefine cmfd : list) {
				//校验一下
				if (!AbstractRuleMethodInterceptor.class.isAssignableFrom(ds.getFilterClass())){
					throw new RuntimeException(ds.getFilterClass().getName()+" is not subclass of "+AbstractRuleMethodInterceptor.class);
				}
				cmfd.setPriority(ds.getPriority());
				cmfd.setFilterClazz(ds.getFilterClass());
				
				//設置filterinstance
				cmfd.setFilterInstance(instance);
				
				defList.add(cmfd);
			}
		}
	}

//	private String filterProperty(String applyTo) {
//		/**
//		 * 这里代码有点丑，先这样了，暂时用Extension.propertyFilter
//		 */
//		if (Extension.propertyFilter!=null)
//			return Extension.propertyFilter.filte(applyTo);
//		else
//			return applyTo;
//	}

	private AbstractRuleMethodInterceptor getOrCreateFilterInstance(Class filterClazz,
			HashMap<Class, AbstractRuleMethodInterceptor> cache) {
		AbstractRuleMethodInterceptor inteceptor = cache.get(filterClazz);
		if (inteceptor == null){
			try{
				inteceptor = (AbstractRuleMethodInterceptor) filterClazz.newInstance();
				PluginEnvirement.INSTANCE.resolveRefAnnotation(inteceptor);
				cache.put(filterClazz, inteceptor);
			}catch(Exception e){
				throw new RuntimeException("can't init object :"+filterClazz.getName(),e);
			}
		}
		return inteceptor;
	}

	/**
	 * 过滤出来，排个序，返回
	 * @param c
	 * @return
	 */
	public List<RuleCallFilterDefine> getMatchedDefinesForClass(Class c) {
		List ret = new ArrayList();
		for (RuleCallFilterDefine o : defList) {
			if (o.matchClazz(c.getName())) {
				ret.add(o);
			}
		}
		SortUtil.sort(ret, (o1, o2) -> {
			return ((RuleCallFilterDefine) o1).getPriority() > ((RuleCallFilterDefine) o2).getPriority();
		});
		return ret;
	}
}
