package net.jplugin.core.kernel.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 * <pre>
 * 
 * JPlugin支持的引用模式的annotation包括：
 * @RefLogger
 * @RefMybatis
 * @RefRuleService
 * @RefRemoteProxy（待实现）
 * @RefConfig
 *</pre>
 * @author Administrator
 *
 */
public class AnnotationResolveHelper {
	private IAnnoForAttrHandler[] handlers;
	private Class[] annoClassArr; 
	private List<Object> toResolveList=new LinkedList();
	private PluginEnvirement pluginEnvirement;
	
	public AnnotationResolveHelper(PluginEnvirement pe) {
		this.pluginEnvirement = pe;
	}

	public void resolveHistory(){
		for (Object o:toResolveList){
			resolveOne(o);
		}
		toResolveList.clear();
	}
	
	public final void valid(){
		IAnnoForAttrHandler[] handlers = PluginEnvirement.getInstance().getExtensionPoint(net.jplugin.core.kernel.Plugin.EP_ANNO_FOR_ATTR).getExtensionObjects(IAnnoForAttrHandler.class);
		
		//valid
		HashSet checkSet = new HashSet();
		for (IAnnoForAttrHandler h:handlers){
			if (checkSet.contains(h.getAnnoClass())){
				throw new RuntimeException("Duplicate handler for annotation class:"+h.getAnnoClass().getName());
			}else{
				checkSet.add(h.getAnnoClass());
			}
		}
	}
	

	/**
	 * 处理annotation标记过的属性
	 */
	public final void resolveBatch(Collection<Object> objList) {
		if (objList==null) 
			return;
		for (Object obj:objList){
			resolveOne(obj);
		}
	}
	
	public final void resolveOne(Object obj){
		//初始化以前先保留
		if (this.pluginEnvirement.getStateLevel() < PluginEnvirement.STAT_LEVEL_INITING){
			this.toResolveList.add(obj);
			return;
		}
		//初始化以后就操作
		if (handlers ==null) 
			init();
	
		if (String.class.equals(obj.getClass()))
			return;

		List<Field> fields = ReflactKit.getAllFields(obj);
		for (Field field : fields) {
			for (int i = 0; i < annoClassArr.length; i++) {
				if (field.isAnnotationPresent(annoClassArr[i])) {
					handleAnnoted(obj, field, handlers[i]);
				}
			}
		}
	}
	



	private void init() {
		handlers = PluginEnvirement.getInstance().getExtensionPoint(net.jplugin.core.kernel.Plugin.EP_ANNO_FOR_ATTR).getExtensionObjects(IAnnoForAttrHandler.class);
		annoClassArr = new Class[handlers.length];
		for (int i=0;i<handlers.length;i++){
			annoClassArr[i] = handlers[i].getAnnoClass();
		}
	}



	private void handleAnnoted(Object obj, Field field, IAnnoForAttrHandler h) {
		Object oldV;
		oldV = ReflactKit.getFieldValueForce(field,obj);
		
		if (oldV!=null){
			if ( Modifier.isStatic(field.getModifiers()))
				return;
			else
				throw new RuntimeException( "None static JPlugin annotated attribute value must be init as null");
		}
		
		if (!h.getAttrClass().isAssignableFrom(field.getType()))
			throw new RuntimeException( "The type of annotated attribute "+h.getAnnoClass().getName()+" must be subclass of "+h.getAttrClass()+" ,"+obj.getClass().getName()+"."+field.getName());
		
		Annotation anno = field.getAnnotation(h.getAnnoClass());
		Object newV = h.getValue(obj,field.getType(),anno);
		if (newV==null) 
			throw new RuntimeException( "Annotated attribute value retrived null: "+h.getAnnoClass().getName()+" ,"+obj.getClass().getName()+"."+field.getName());
		else{
			ReflactKit.setFieldValueForce(field,obj,newV);
		}
	}
}
