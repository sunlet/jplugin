package net.jplugin.common.kits;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.PritiveKits.Transformer;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-2-5 上午08:58:51
 **/

public class ObjectKit {
	public static <T> List<T> findList(Object o,String path, Class<T> t) {
		return (List<T>) findObject(o,path);
	}

	public static <T> Map<String, T> findMap(Object o,String path, Class<T> t) {
		return (Map<String, T>) findObject(o,path);
	}

	public static Object findObject(Object o,String path) {
		PathSegment[] segs = parse(path);
		Object tmp=o;
		try{
			for (int i=0;i<segs.length;i++){
				tmp = findBySeg(tmp,segs[i]);
				if (tmp==null){
					return null;
				}
			}
		}catch(Exception e){
			throw new RuntimeException("Error to get object,path="+path +" o="+o.getClass().getName(),e);
		}
		return tmp;
	}

	/**
	 * @param tmp
	 * @param pathSegment
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static Object findBySeg(Object o, PathSegment p) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (!StringKit.isNull(p.tag)){
			o = getProperty(o,p.tag);
		}
		
		if (p.attr == null){
			return o;
		}
		//列表 或者数组（暂不支持数组）
		if (p.attr.equals(PathSegment.ATTR_IDX)){
			int pos = Integer.parseInt(p.value);
			if (pos<0){
				pos = ((List)o).size() + pos;
			}
			return ((List)o).get(pos);
		}else{
			List list = (List) o;
			for (Object item:list){
				Object ret = getProperty(item,p.attr);
				
				Transformer transformer = PritiveKits.getTransformer(ret.getClass());
				if (transformer==null){
					throw new RuntimeException(ret.getClass()+" is not a primative type");
				}
				if (p.value.equals(transformer.convertToString(ret))){
					return item;
				}
			}
		}
		return o;
	}
	
	

	/**
	 * @param item
	 * @param attr
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static Object getProperty(Object item, String attr) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (item instanceof Map){
			return ((Map)item).get(attr);
		}else{
			Method m = ReflactKit.getGetterMethod(item.getClass(),attr);
			return m.invoke(item);
		}
	}

	public static PathSegment[] parse(String path) {
		if (path==null || path.trim().equals("")){
			throw new RuntimeException("error path:"+path);
		}
		String[] paths = path.split("/");
		PathSegment[] ret = new PathSegment[paths.length];
		for (int i=0;i<ret.length;i++){
			ret[i]=new PathSegment(paths[i]);
		}
		return ret;
	}

	static class PathSegment {
		static final String ATTR_IDX = "__INDEX";
		String tag;
		String attr;
		String value;

		public String toString() {
			return "tag=[" + tag + "] attr=[" + attr + "] value=[" + value+"]";
		}

		// abc[@xxx=yyy];
		// abc[@xxx="yyy"]
		// abc[@xxx='yyy']
		// abc[1]
		// abc[-1]
		public PathSegment(String s) {
			if (s==null || s.equals("")){
				throw new RuntimeException("error path:"+s);
			}
			s = s.trim();
			if (s.endsWith("]")) {
				int leftKHPos = s.indexOf("[");
				// 获取tag
				tag = s.substring(0, leftKHPos).trim();
				// 获取attr
				String attrString = s.substring(leftKHPos + 1, s.length() - 1);
				if (attrString.startsWith("@")) {
					// 中括号内是条件
					int dhPos = attrString.indexOf("=");
					attr = decode(attrString.substring(0, dhPos));
					attr = attr.substring(1);//去掉@
					attr = attr.trim();
					value = decode(attrString.substring(dhPos + 1));

					if (attr == null || attr.equals("")) {
						throw new RuntimeException("path error:" + s);
					}
					if (value == null || value.equals("")) {
						throw new RuntimeException("path error:" + s);
					}
				} else {
					// 中括号内是序号
					attrString = attrString.trim();
					if (!StringKit.isNumAllowNig(attrString)) {
						throw new RuntimeException("path error:" + s);
					}
					attr = ATTR_IDX;
					value = attrString;
				}
			}else{
				tag = s.trim();
			}
		}

		/**
		 * 去除空格，去除单引号双引号 转义符去掉
		 * 
		 * @param name
		 * @return
		 */
		private String decode(String s) {
			if (s == null)
				return null;
			s = s.trim();
			char c1 = s.charAt(0);
			char c2 = s.charAt(s.length()-1);
			
			//判断用双引号或者单引号括起来，或者不带引号
			if (!((c1=='\'' && c2=='\'') || (c1=='"' && c2=='"') || 
			    ((c1!='\'' && c1!='"')&&(c2!='\'' && c2!='"')))){
				throw new RuntimeException("path error:" + s);
			}

			if (s.startsWith("'")) {
				if (!s.endsWith("'") || s.length() < 2) {
				}
				s = s.substring(1, s.length() - 1);
			} else if (s.startsWith("\"")) {
				if (!s.endsWith("\"")  || s.length() < 2) {
					throw new RuntimeException("path error:" + s);
				}
				s = s.substring(1, s.length() - 1);
			}

			// 转义符赞不处理
			return s;
		}
	}
	
	public static void main(String[] args) {
		print("[11]");
		print("a/b/c/d/e");
		print("a");
		AssertKit.assertException(new Runnable() {
			public void run() {
				parse("");
			}
		});
		print("b[1]/b[-1]/b[@a=11]/b[@a='11']/b[@a=\"11\"]");
		print("  b  [@ a =  \"11\"   ]   ");
		print("b[@a=\"11\"\"]");
		
		AssertKit.assertException(new Runnable() {
			public void run() {
				print("b[@a='11]");			}
		});
		AssertKit.assertException(new Runnable() {
			public void run() {
				print("b[@a=11']");			}
		});
		AssertKit.assertException(new Runnable() {
			public void run() {
				print("b[@a=\"11]");
			}
		});
		AssertKit.assertException(new Runnable() {
			public void run() {
				print("b[@a=11\"]");
			}
		});
		AssertKit.assertException(new Runnable() {
			public void run() {
				print("b[@a='11\"]");
			}
		});

		
		Map<String, Object> map1=new HashMap<String, Object>();
		Map<String, Object> map2=new HashMap<String, Object>();
		List<String> list1= new ArrayList();
		list1.add("0");
		list1.add("1");
		list1.add("2");
		map2.put("x", "xvalue");
		
		map1.put("a", "avalue");
		map1.put("map", map2);
		map1.put("list", list1);
		
		List list2= new ArrayList();
		Map m = new HashMap();
		m.put("age", "100");
		list2.add(m);
		m=new HashMap();
		m.put("age", 200);
		list2.add(m);
		Person p = new Person();
		p.age = 300;
		list2.add(p);
		map1.put("list2", list2);
		
		
		AssertKit.assertEqual(findObject(map1, "a"),"avalue");
		AssertKit.assertEqual(findObject(map1, "map/x"),"xvalue");
		AssertKit.assertEqual(findObject(map1, "list/[1]"),"1");
		AssertKit.assertEqual(findObject(map1, "list[-1]"),"2");
		AssertKit.assertEqual(findObject(map1, "list2[@age=200]/age"),200);
		AssertKit.assertEqual(findObject(map1, "list2/[@age=100]/age"),"100");
		AssertKit.assertEqual(findObject(map1, "list2/[@age='300']/age"),300);
	}
	static class Person{
		int age;

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
		
	}
	/**
	 * @param parse
	 */
	private static void print(String path) {
		System.out.println(" path:"+path);
		PathSegment[] segs = parse(path);
		for (int i=0;i<segs.length;i++){
			System.out.println(segs[i]);
		}
		System.out.println();
	}
	
}