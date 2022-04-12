package test.net.jplugin.common.kits;

import test.net.jplugin.common.kits.JsonTest.Person.TP;
import net.jplugin.common.kits.JsonKit;

public class JsonTest {
	
	public static void main(String[] args) {
		Person p = new Person();
		p.age=10;
		p.name="zh";
		p.type = TP.A;
		
		String s = JsonKit.object2Json(p);
		System.out.println(s);
		String str = "{\"age\":\"10\",\"name\":\"zh\",\"type\":\"A\", \"good\":\"true\" }";
		Person oo = (Person) JsonKit.json2Object(str, Person.class);
		System.out.println(JsonKit.object2Json(oo));
	}
	static class Person {
		enum TP{A,B,C}
		int age;
		String name;
		TP type;
		boolean good;
		
		public boolean isGood() {
			return good;
		}
		public void setGood(boolean good) {
			this.good = good;
		}
		public TP getType() {
			return type;
		}
		public void setType(TP type) {
			this.type = type;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
}
