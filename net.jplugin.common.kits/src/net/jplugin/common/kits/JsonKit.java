package net.jplugin.common.kits;

import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.jplugin.common.kits.PritiveKits.Transformer;
/**
 * Json鎿嶄綔宸ュ叿锟�?
 * @author liyy
 * @date 2014-05-20
 */
public class JsonKit {
//	private static Logger logger = Logger.getLogger(JsonKit.class);
//	private static final ObjectMapper mapper = new ObjectMapper();
	
	private static final ObjectMapper mapper;

	static {
	   mapper = new ObjectMapper();
	   mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	//<<< 2018年8月28日修改
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object json2Object4Type(String json, Type type) {
		Object object = null;
		try {
			if (type instanceof Class) {
				object = JsonKit.json2Object(json, (Class) type);
			} else {
				JavaType javaType = mapper.getTypeFactory().constructType(type);
				object = mapper.readValue(json, javaType);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return object;
	}
	
	public static Object json2Object4TypeEx(String val, Type type) {
		if (val==null || val.equals("")){
			return null;
		}
		Class clz=null;
		Transformer trans = null;
		if (type instanceof Class){
			clz = (Class) type;
			trans = PritiveKits.getTransformer(clz);
		}
		
		if (trans!=null ){
			return trans.fromString(clz, val);
		}else{
			return json2Object4Type(val,type);
		}	
	}
	//>>>2018年8月28日修改
	
	public static String object2Json(Object object) {
		StringWriter writer = new StringWriter();
		try {
			if (object != null)
				mapper.writeValue(writer, object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}

	public static <T> T json2Object(String json, Class<T> klass) {
		T object = null;
		try {
			if (json != null && json.length() > 0)
				object = mapper.readValue(json, klass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return object;
	}

	public static Map json2Map(String json) {
		Map m = null;
		try {
			if (json != null && json.length() > 0)
				m = mapper.readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return m;
	}

	public static List<Map> json2ListMap(String json) {
		List m = null;
		try {
			if (json != null && json.length() > 0)
				m = mapper.readValue(json, List.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return m;
	}
	
	public static <T> List<T> json2ListBean(String json, Class<T> beanClass) {
		List<T> m = null;
		try {
			if (json != null && json.length() > 0){
				JavaType javaType = getCollectionType(ArrayList.class, beanClass);
				m = mapper.readValue(json, javaType);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return m;
	}
	
	public static <K, V> Map json2MapBean(String json, Class<K> keyClass, Class<V> beanClass) {
		Map m = null;
		try {
			if (json != null && json.length() > 0){
				JavaType javaType = getCollectionType(HashMap.class, keyClass, beanClass);
				m = mapper.readValue(json, javaType);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return m;
	}
	public static void main1(String[] args) {
		TestBean o = new TestBean();
		TestBean o2 = new TestBean();
		TestBean[] arr = new TestBean[]{o,o2};
		
		System.out.println(object2Json(arr));
		
		String json = object2Json(arr);
		TestBean[] ret  = json2Object(json, TestBean[].class);
		System.out.println(object2Json(ret));
		
		List list = json2Object(json, java.util.List.class);
		
		System.out.println(object2Json(list));
	}
	public static void main(String[] args) {
		TestBean[] arr = new TestBean[2];
		arr[0]= new TestBean();
		arr[1] = new TestBean();
		String json = JsonKit.object2Json(arr);
		System.out.println(json);
		
		Object obj = JsonKit.json2ObjectEx(json,arr.getClass());
		System.out.println(JsonKit.object2JsonEx(obj));
	}
	
	public static void main2(String[] args) throws Exception {
		
		int[] arr = new int[]{1,2,3};
		ArrayList al = new ArrayList();
		al.add(1);
		al.add(2);
		al.add("3");
		
		
		System.out.println("int arr = "+object2Json(arr));
		System.out.println("list = "+object2Json(arr));
		System.out.println("str = "+object2Json("111"));
		System.out.println("int = "+object2Json(111));
		System.out.println("date = "+object2Json(new Date()));

		
		String json = "[{\"name\":\"aaa\",\"age\":\"15\"}, {\"name\":\"bbb\",\"age\":\"25\"}]";
		List<TestBean> lst = json2ListBean(json, TestBean.class);
		System.out.println(lst);
		System.out.println(lst.get(0).getName());
		
		
		json = "{\"t1\":{\"name\":\"aaa\",\"age\":\"15\"}, \"t2\":{\"name\":\"bbb\",\"age\":\"25\"}}";
		Map<String, TestBean> mst = json2MapBean(json, String.class, TestBean.class);
		System.out.println(mst);
		System.out.println(mst.get("t1").getName());
	}

	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 * 锟斤拷强锟斤拷转锟斤拷锟斤拷锟斤拷锟节伙拷锟斤拷锟斤拷锟斤拷直锟斤拷转锟斤拷锟街凤拷锟斤拷
	 * @param val
	 * @param clz
	 * @return
	 */
	public static <T> Object json2ObjectEx(String val, Class<T> clz) {
		if (val==null || val.equals("")){
			return null;
		}
		Transformer trans = PritiveKits.getTransformer(clz);
		if (trans!=null ){
			return trans.fromString(clz, val);
		}else{
			return json2Object(val,clz);
		}
	}
	
	public static String object2JsonEx(Object obj){
		if (obj==null){
			return "";
		}
		Transformer trans = PritiveKits.getTransformer(obj.getClass());
		if (trans!=null ){
			return trans.convertToString(obj);
		}else{
			return object2Json(obj);
		}
	}
}

class TestBean {
	private String name;
	private String age;

	public TestBean() {
		name = "zs";
		age="10";
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	
}