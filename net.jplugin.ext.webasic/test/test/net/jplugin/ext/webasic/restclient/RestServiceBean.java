package test.net.jplugin.ext.webasic.restclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jplugin.ext.webasic.api.Para;

public class RestServiceBean {
	/* (non-Javadoc)
	 * @see test.net.jplugin.ext.webasic.restclient.IService#add(int, int)
	 */
	public int add (int a,int b){
		return a+b;
	}
	
	/* (non-Javadoc)
	 * @see test.net.jplugin.ext.webasic.restclient.IService#add(java.lang.String, java.lang.String)
	 */
	public String addString(String a,@Para(name="b") String b){
		return a+b;
	}
	
	/* (non-Javadoc)
	 * @see test.net.jplugin.ext.webasic.restclient.IService#getBeanList()
	 */
	public List<Bean> getBeanList(){
		List<Bean> list = new ArrayList<RestServiceBean.Bean>();
		list.add(new Bean("zhangsan",10));
		list.add(new Bean("lisi",20));
		return list;
	}
	
	/* (non-Javadoc)
	 * @see test.net.jplugin.ext.webasic.restclient.IService#getBeanMap()
	 */
	public Map<String,Bean> getBeanMap(){
		Map<String,Bean> m = new HashMap<String, RestServiceBean.Bean>();
		m.put("zhangsan", new Bean("zhangsan",10));
		m.put("lisi", new Bean("lisi",20));
		return m;
	}
	
	public static class Bean{
		String name;
		int age;
		public Bean(){}
		public Bean(String n,int a){
			this.name = n;
			this.age = a;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	}
	
}
