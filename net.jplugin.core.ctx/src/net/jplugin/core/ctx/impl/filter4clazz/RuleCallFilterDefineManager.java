package net.jplugin.core.ctx.impl.filter4clazz;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.SortUtil;
import net.jplugin.core.ctx.api.AbstractRuleMethodInterceptor;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class RuleCallFilterDefineManager {
	
	public static RuleCallFilterDefineManager INSTANCE = new RuleCallFilterDefineManager();

	RuleCallFilterDefineBean[] defSetList;
	List<RuleCallFilterDefine> defList;

	public void initialize() {
		defSetList = PluginEnvirement.getInstance().getExtensionObjects(net.jplugin.core.ctx.Plugin.EP_RULE_METHOD_INTERCEPTOR,RuleCallFilterDefineBean.class);
		defList = new ArrayList();
		for (RuleCallFilterDefineBean ds : defSetList) {
			List<RuleCallFilterDefine> list ;
			try{
				list = RuleCallFilterDefine.parse(ds.getApplyTo());
			}catch(Exception e){
				throw new RuntimeException("Binding annotation on "+ds.getClass().getName()+" error.",e);
			}
			for (RuleCallFilterDefine cmfd : list) {
				//校验一下
				if (!AbstractRuleMethodInterceptor.class.isAssignableFrom(ds.getFilterClass())){
					throw new RuntimeException(ds.getFilterClass().getName()+" is not subclass of "+AbstractRuleMethodInterceptor.class);
				}
				cmfd.setPriority(ds.getPriority());
				cmfd.setFilterClazz(ds.getFilterClass());
				defList.add(cmfd);
			}
		}
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
