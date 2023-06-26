package jplugincoretest.net.jplugin.ext.webasic.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.ext.webasic.api.Para;
import net.jplugin.ext.webasic.impl.restm.RestMethodState;

public class ServiceBean {
	/* (non-Javadoc)
	 * @see test.net.jplugin.ext.webasic.restclient.IService#add(int, int)
	 */
	public int add (int a,int b){
		return a+b;
	}
	
	public Integer testFullMatch(@Para(name="_FULL_MATCH_") int  a){
		return a+1;
	}
	
	/* (non-Javadoc)
	 * @see test.net.jplugin.ext.webasic.restclient.IService#add(java.lang.String, java.lang.String)
	 */
	public String addString(String a,@Para(name="b") String b){
		return a+b;
	}

	public String addStringNoRequired(String a,@Para(name="b",required=false) String b){
		return a+b;
	}

	
	/* (non-Javadoc)
	 * @see test.net.jplugin.ext.webasic.restclient.IService#getBeanList()
	 */
	public List<Bean> getBeanList(){
		List<Bean> list = new ArrayList<ServiceBean.Bean>();
		list.add(new Bean("zhangsan",10));
		list.add(new Bean("lisi",20));
		return list;
	}
	
	/* (non-Javadoc)
	 * @see test.net.jplugin.ext.webasic.restclient.IService#getBeanMap()
	 */
	public Map<String,Bean> getBeanMap(){
		Map<String,Bean> m = new HashMap<String, ServiceBean.Bean>();
		m.put("zhangsan", new Bean("zhangsan",10));
		m.put("lisi", new Bean("lisi",20));
		return m;
	}
	
	public void ex(){
		throw new RuntimeException("test ex");
	}
	public void remoteEx(){
		RemoteExecuteException ex = new RemoteExecuteException("100","test remote ex",new Exception("ccc"));
		ex.setNeedLog(false);
		throw ex;
	}
	
	public void indirectEx(){
		RestMethodState.setSuccess(false);
		RestMethodState.setCode("-100");
		RestMethodState.setMessage("indirectmsg");
	}

	
	public static class Bean implements Serializable{
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
