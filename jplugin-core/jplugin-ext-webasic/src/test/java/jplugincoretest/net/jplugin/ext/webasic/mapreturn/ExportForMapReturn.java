package jplugincoretest.net.jplugin.ext.webasic.mapreturn;

import java.util.Map;
import java.util.TreeMap;

public class ExportForMapReturn {
	
	public Map<String,Bean> getResult(){
		TreeMap<String, Bean> m = new TreeMap<String, ExportForMapReturn.Bean>();
		m.put("x", new Bean("x","y"));
		m.put("a", new Bean("a","b"));
		m.put("u", new Bean("x","y"));
		m.put("w", new Bean("a","b"));
		m.put("o", new Bean("a","b"));
		m.put("b", new Bean("a","b"));
		return m;
	}
	
	
	public static class Bean{
		String name;
		String age;
		public Bean(String n, String a) {
			name = n;
			age = a;
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
}
