package net.jplugin.core.das.mybatis.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;

import org.apache.ibatis.binding.BindingException;

import net.jplugin.core.das.mybatis.api.MyBatisServiceFactory;

public class MapperProxyFactory {
	//这里并发不高，只在初始化使用，用Hashtable
	//发生重新创建Proxy对象，也是没有问题的
	static Hashtable<String, Hashtable<Class,Object>> mappers=new Hashtable();
	
	public static Object getMapper(String dataSourceName,Class intfClazz){
		Hashtable<Class, Object> ht = mappers.get(dataSourceName);
		if (ht==null){
			ht = new Hashtable<>();
			mappers.put(dataSourceName,ht);
		}
		Object o = ht.get(intfClazz);
		if (o!=null) 
			return o;
		else{
			o = Proxy.newProxyInstance(intfClazz.getClassLoader(), new Class[]{intfClazz}, new InvocationHandler4Mapper(dataSourceName,intfClazz));
			ht.put(intfClazz, o);
			return o;
		}
	}
	
	static class InvocationHandler4Mapper implements InvocationHandler{
		private String dsName;
		private Class intf;

		InvocationHandler4Mapper(String dataSourceName,Class intfClazz){
			this.dsName = dataSourceName;
			this.intf = intfClazz;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object real;
			try{
				real = MyBatisServiceFactory.getService(dsName).getMapper(intf);
			}catch(BindingException b){
				throw new RuntimeException("Perhaps the interface ["+intf.getName()+"] is not binding to datasource ["+dsName+"]");
			}
			return method.invoke(real, args);
			
		}
	}
}
