package net.jplugin.core.kernel.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class Test{
	
	public static void main(String[] args) {
		System.out.println(getParameterizedIntfArg(TT.class, IExtensionFactory.class));
		
		
//		System.out.println();
	}
	
	
	
	public static Type getParameterizedIntfArg(Class cls,Class intf) {
		Type[] intfClazzs = cls.getGenericInterfaces();
		
		for (Type tp:intfClazzs) {
			ParameterizedType parameterizedType = (ParameterizedType) tp;
	        Type actualtype = parameterizedType.getActualTypeArguments()[0];
	        return actualtype;
		}
		return null;
	}
	
	
	public static class TT implements IExtensionFactory<Map>{

		@Override
		public Map create() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
}