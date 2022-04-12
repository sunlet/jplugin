package net.jplugin.common.kits;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 下午04:20:58
 **/

public class PritiveKits {
	public static boolean support(Class clz){
		return getTransformer(clz)!=null;
	}
	
	public static Transformer getTransformer(Class clz){
		if (clz.isEnum()){
			return transformerMap.get(Enum.class);
		}else{
			return transformerMap.get(clz);
		}
	}
	
	private static Map<Class,Transformer> transformerMap =new HashMap<Class, Transformer>();
	static{
		transformerMap.put(Integer.class,new IntegerTrans() );
		transformerMap.put(int.class,new IntegerTrans() );
		transformerMap.put(Long.class,new LongTrans() );
		transformerMap.put(long.class,new LongTrans() );
		transformerMap.put(Double.class,new DoubleTrans() );
		transformerMap.put(double.class,new DoubleTrans() );
		transformerMap.put(Float.class,new FloatTrans() );
		transformerMap.put(float.class,new FloatTrans() );
		transformerMap.put(Date.class,new DateTrans());
		transformerMap.put(String.class,new StringTrans());
		transformerMap.put(Enum.class,new EnumTrans());
		transformerMap.put(Boolean.class,new BooleanTrans() );
		transformerMap.put(boolean.class,new BooleanTrans() );
	}
	
	
	
	public static class Transformer{
		public String convertToString(Object obj){
			return null;
		}
		public Object fromString(Class t,String s){
			return null;
		}
	}
	
	public static class StringTrans extends Transformer{
		public String convertToString(Object obj){
			return (String) obj;
		}
		public Object fromString(Class t,String s){
			return s;
		}
	}
	
	public static class IntegerTrans extends Transformer{
		@Override
		public String convertToString(Object obj){
			return obj.toString();
		}
		@Override
		public Object fromString(Class t,String s){
			return Integer.parseInt(s);
		}
	}
	public static class LongTrans extends Transformer{
		@Override
		public String convertToString(Object obj){
			return obj.toString();
		}
		@Override
		public Object fromString(Class t,String s){
			return Long.parseLong(s);
		}
	}
	public static class DoubleTrans extends Transformer{
		@Override
		public String convertToString(Object obj){
			return obj.toString();
		}
		@Override
		public Object fromString(Class t,String s){
			return Double.parseDouble(s);
		}
	}
	public static class FloatTrans extends Transformer{
		@Override
		public String convertToString(Object obj){
			return obj.toString();
		}
		@Override
		public Object fromString(Class t,String s){
			return Float.parseFloat(s);
		}
	}
	public static class DateTrans extends Transformer{
		@Override
		public String convertToString(Object obj){
			return  String.valueOf(((Date)obj).getTime());
		}
		@Override
		public Object fromString(Class t,String s){
			return new Date(Long.parseLong(s));
		}
	}
	public static class BooleanTrans extends Transformer{
		@Override
		public String convertToString(Object obj){
			if ((Boolean)obj){
				return "true";
			}else{
				return "false";
			}
		}
		@Override
		public Object fromString(Class t,String s){
			if ("true".equals(s)){
				return true;
			}else{
				return false;
			}
		}
	}
	public static class EnumTrans extends Transformer{
		@Override
		public String convertToString(Object obj){
			return obj.toString();
		}
		@Override
		public Object fromString(Class t,String s){
			return Enum.valueOf(t, s);
		}
	}
	
	enum EE{A,B,C}
	public static void main(String[] args) {
		String s="aaa";
		int i=1;
		Integer ii=1;
		long l=1;
		Long ll=(long)1;
		Double d =1.1;
		Float f = 1f;
		float ff = 1f;
		Date dd = new Date();
		EE  e = EE.A;
		
		Object[] arr = new Object[]{s,i,i,l,ll,d,f,ff,dd,e};
		
		for (int iii=0;iii<arr.length;iii++){
			 Class cls = arr[iii].getClass();
			 Object obj = arr[iii];
			 Transformer trans = getTransformer(cls);
			System.out.println(trans.convertToString(trans.fromString(cls,trans.convertToString(obj))));
		}
	}
	
}
