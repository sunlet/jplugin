package net.jplugin.core.kernel.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.kernel.api.IObjectResolver;
import net.jplugin.core.kernel.api.Initializable;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 * <pre>
 * 能够自动
 * JPlugin支持的引用模式的annotation包括：
 * &#64;RefLogger
 * &#64;RefMybatis
 * &#64;RefRuleService
 * &#64;RefRemoteProxy（待实现）
 * &#64;RefConfig
 * </pre>
 * 
 * @author Administrator
 *
 */
public class AnnotationResolveHelper {

	private List<IObjectResolver> resolverList = new ArrayList(2);
	private List<Object> toResolveList = new LinkedList();
	private PluginEnvirement pluginEnvirement;

	private List<Object> toInitingList = new LinkedList<>();


	public AnnotationResolveHelper(PluginEnvirement pe) {
		this.pluginEnvirement = pe;

		//增加默认的resolver
		this.resolverList.add(new DefaultObjectResolver());
	}



	public void initHistory(){
		for (Object o:toInitingList){
			((Initializable)o).initialize();
		}
		toInitingList.clear();
	}

	public void addObjectResolver(IObjectResolver ior){
		if (PluginEnvirement.getInstance().getStateLevel()>= PluginEnvirement.STAT_LEVEL_RESOLVING_HIST) {
			throw new RuntimeException("Resolver must be added before state:STAT_LEVEL_RESOLVING_HIST");
		}else{
			this.resolverList.add(ior);
		}
	}

	public void resolveHistory() {
		/**
		 * 这里用一个set做一个排重。为了避免用户加了调用了resolve方法，同时框架也处理了。对冲突进行避免。
		 */
		Set<Object> set = new HashSet();
		for (Object o : toResolveList) {
			if (set.contains(o))
				continue;
			set.add(o);
			resolveOne(o);
		}
		toResolveList.clear();
		set.clear();
	}

	public final void valid() {
		IAnnoForAttrHandler[] handlers = PluginEnvirement.getInstance()
				.getExtensionPoint(net.jplugin.core.kernel.Plugin.EP_ANNO_FOR_ATTR)
				.getExtensionObjects(IAnnoForAttrHandler.class);

		// valid
		HashSet checkSet = new HashSet();
		for (IAnnoForAttrHandler h : handlers) {
			if (checkSet.contains(h.getAnnoClass())) {
				throw new RuntimeException("Duplicate handler for annotation class:" + h.getAnnoClass().getName());
			} else {
				checkSet.add(h.getAnnoClass());
			}
		}
	}

	/**
	 * 处理annotation标记过的属性
	 */
	public final void resolveBatch(Collection<Object> objList) {
		if (objList == null)
			return;
		for (Object obj : objList) {
			resolveOne(obj);
		}
	}

	public final void resolveOne(Object obj) {
		// 初始化以前先保留
		if (this.pluginEnvirement.getStateLevel() < PluginEnvirement.STAT_LEVEL_RESOLVING_HIST) {
			this.toResolveList.add(obj);
			return;
		}

		//
		for (int i=0;i<this.resolverList.size();i++){
			this.resolverList.get(i).resolve(obj);
		}
		
		//
		if (obj instanceof Initializable){
			if (this.pluginEnvirement.getStateLevel() < PluginEnvirement.STAT_LEVEL_INITING){
				//暂缓初始化
				toInitingList.add(obj);
			}else {
				((Initializable) obj).initialize();
			}
		}
	}


}
