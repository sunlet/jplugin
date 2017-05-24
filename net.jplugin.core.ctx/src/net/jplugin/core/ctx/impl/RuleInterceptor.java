/*******************************************************************************
 * $Header: /bpmsrc/CVSROOT/develop/src/bps-server/com.primeton.workflow.engine.common/src/com/primeton/workflow/api/proxy/EngineInterceptor.java,v 1.1 2010/06/23 17:35:17 yangjf Exp $
 * $Revision: 1.1 $
 * $Date: 2010/06/23 17:35:17 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2007-7-26
 *******************************************************************************/

package net.jplugin.core.ctx.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.api.RuleMetaException;

public class RuleInterceptor implements InvocationHandler {
	Class<?> interfaceClass;
	RuleInvocationHandler handler;
	Object oldService;
	MethodMetaLocater locator;

	public RuleInterceptor(){}
	
	public RuleInterceptor(Class<?> cls) {
		interfaceClass = cls;
		valid();
		locator = new MethodMetaLocater(cls);
	}

	/**
	 * 获取一个PROXY的实例
	 * @param clazz
	 * @param service
	 * @return
	 */
	public static Object getProxyInstance(Class clazz,Object oldImpl,RuleInvocationHandler handler){
		RuleInterceptor ei = new RuleInterceptor(clazz);
		ei.handler = handler;
		ei.oldService = oldImpl;
		return Proxy.newProxyInstance(oldImpl.getClass().getClassLoader(), new Class[]{clazz}, ei);
	}
	
	public void setInterfaceCls(Class cls){
		this.interfaceClass = cls;
	}

	/**
	 * 正确性检查
	 */
	public void valid() {
		//
		if (!interfaceClass.isInterface()) {
			throw new RuleMetaException("cls " + interfaceClass
					+ " must be interface!");
		}

//		for (Method m : interfaceClass.getMethods()) {
//			
//			Rule meta = m.getAnnotation(Rule.class);
//
//			if (meta == null)
//				throw new RuleMetaException("Can't find meta!" + m);
//		
//			
////			if (isTransactionedName(m.getName())){
////				if (meta.methodType()==Rule.TxType.ANY) 
////					throw new RuleMetaException("methodType not right:"+m);
////			}
//		}
	}

	/**
	 * @param name
	 * @return
	 */
	private boolean isTransactionedName(String name) {
		if (name.startsWith("get") || name.startsWith("query")|| name.startsWith("list") ||name.startsWith("find")
				|| name.startsWith("common"))
			return false;
		// 判断第一个单词为IS
		if (name.startsWith("is")) {
			char c2 = name.charAt(2);
			if (c2 >= 'A' && c2 <= 'Z')
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Rule meta = locator.findMeta(method);
		if (meta!=null)
			return handler.invoke(proxy,oldService,method,args,meta);
		else {
			try {
				return method.invoke(oldService, args);
			} catch (Throwable th) {
				if (th instanceof InvocationTargetException) {
					throw ((InvocationTargetException) th).getTargetException();
				} else 
					throw th;
			}
		}
	}
	
	
	static class MethodMetaLocater {
		HashMap<String, Rule> singleMetaMap = new HashMap<String, Rule>();

		HashMap<String, List<MethodParaInfo>> dupMetaMap = new HashMap<String, List<MethodParaInfo>>();

		public String toString(){
			StringBuffer sb = new StringBuffer();
			for (Entry<String,List<MethodParaInfo>> e: dupMetaMap.entrySet()){
				sb.append(e.getKey());
				sb.append("  ");
				sb.append(e.getValue());
			}
			
			return sb.toString();
		}
		
		public MethodMetaLocater(Class cls) {

			List<Method> dupMethod = getDupMethods(cls);
			List<Method> singleMethod = getSingleMethods(cls);

			for (Method m : singleMethod) {
				Rule meta = m.getAnnotation(Rule.class);
//				AssertKit.assertNotNull(meta, "meta");
				singleMetaMap.put(m.getName(), (Rule) m
						.getAnnotation(Rule.class));
			}

			for (Method m : dupMethod) {
				Rule meta = m.getAnnotation(Rule.class);
//				AssertKit.assertNotNull(meta, "meta");

				List<MethodParaInfo> list = dupMetaMap.get(m.getName());
				if (list == null) {
					list = new ArrayList<MethodParaInfo>();
					dupMetaMap.put(m.getName(), list);
				}

				list.add(new MethodParaInfo(meta, m.getParameterTypes()));
			}
		}

		public Rule findMeta(Method m){
			Rule ret = singleMetaMap.get(m.getName());
			if (ret!=null) return ret;
			
			List<MethodParaInfo> list = dupMetaMap.get(m.getName());
			if (list==null) return null;
			
			for (MethodParaInfo record:list){
				if (typeMatch(record.paraTypes,m.getParameterTypes())){
					return record.meta;
				}
			}
			return null;
		}
		
		/**
		 * @param paraTypes
		 * @param parameterTypes
		 * @return
		 */
		private boolean typeMatch(Class[] a1, Class<?>[] a2) {
			if (a1.length!=a2.length) return false;
			
			for (int i=0;i<a1.length;i++){
				if (a1[i]!=a2[i]) return false;
			}
			
			return true;
		}

		/**
		 * @return
		 */
		private List<Method> getSingleMethods(Class cls) {
			List<Method> dupm = getDupMethods(cls);
			List<Method> ret = new ArrayList<Method>();

			for (Method m : cls.getMethods()) {
				if (!dupm.contains(m)) {
					ret.add(m);
				}
			}
			return ret;
		}

		/**
		 * @return
		 */
		private List<Method> getDupMethods(Class cls) {
			List<Method> listRet = new ArrayList<Method>();
			HashSet<String> hs = new HashSet<String>();
			HashSet<String> hsDup = new HashSet<String>();
			for (Method m : cls.getMethods()) {
				if (hs.contains(m.getName()))
					hsDup.add(m.getName());
				hs.add(m.getName());
			}
			
			for (Method m : cls.getMethods()) {
				if (hsDup.contains(m.getName())){
					listRet.add(m);
				}
			}

			return listRet;

		}

		static class MethodParaInfo {
			MethodParaInfo(Rule e, Class[] p) {
				meta = e;
				paraTypes = p;
			}
			
			public String toString(){
				StringBuffer sb = new StringBuffer();
				sb.append("{");
				sb.append(meta.actionDesc());
				sb.append(" ");
				for (Class c:paraTypes){
					sb.append(c.getName());	
					sb.append("\t");
				}
				
				sb.append("}");
				return sb.toString();
			}

			Rule meta;

			Class[] paraTypes;
		}
	}
}
