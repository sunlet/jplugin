package jplugincoretest.net.jplugin.ext.webasic.restmethod;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.http.mock.HttpMock;
import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.ext.webasic.api.Para;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 下午02:09:06
 **/

public class RestMethod4Pojo {
	
	public void index(){
		nopara();
	}
	
//	public void aaa(@Para(name="callback") String pp){
//		
//	}
	
	public void nopara(){
	}
	
	public String concact(String a,String b){
		return a+b;
	}
	
//	public String testFullMatchCheck(@Para(name="_FULL_MATCH_") String b){
//		return b;
//	}
	
	public Integer add(int a,int b){
		return a+b;
	}
	


	@Para(name="aa")
	public Integer addWithAnno(@Para(name="a") int a,@Para(name="b") int b){
		return a+b;
	}
	
	public List<Item> query(Item a){
		List<Item > ret = new ArrayList<Item>();
		ret.add(a);
		ret.add(a);
		return ret;
	}

	
	/**
	 * @author Luis
	 *
	 */
	public static class Item {
		String name;
		int age;
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
	
	public static void test(){
		test1();
		test2();
		testConcact();
		testConcactJsonP();
		testAdd();
		testAddWithAnno();
		testQuery();
	}

	/**
	 * 
	 */
	private static void testQuery() {
		HttpMock mock = new HttpMock();
		mock.request.setServletPath("/testremotepojo/query.do");
//		mock.request.setPara("_o", "query");
		Item item = new Item();
		item.setAge(30);
		item.setName("lh");
		mock.request.setPara("arg0",JsonKit.object2JsonEx(item) );
		mock.invoke();
		String result = mock.response.getResult();
		Object ret = ((Map)JsonKit.json2Map(result).get("content")).get("result");
		AssertKit.assertEqual(((List)ret).size(), 2, null);
		System.out.println(mock.response.getResult());
	}

	/**
	 * 
	 */
	private static void testAddWithAnno() {
		HttpMock mock = new HttpMock();
		mock.request.setServletPath("/testremotepojo/addWithAnno.do");
//		mock.request.setPara("_o", "addWithAnno");
		mock.request.setPara("a", "1");
		mock.request.setPara("b", "2");
		mock.invoke();
		String result = mock.response.getResult();
		Object ret = ((Map)JsonKit.json2Map(result).get("content")).get("result");
		AssertKit.assertEqual(ret, 3, null);
		System.out.println(mock.response.getResult());
	}

	/**
	 * 
	 */
	private static void testAdd() {
		HttpMock mock = new HttpMock();
		mock.request.setServletPath("/testremotepojo/add.do");
//		mock.request.setPara("_o", "add");
		mock.request.setPara("arg0", "1");
		mock.request.setPara("arg1", "2");
		mock.invoke();
		String result = mock.response.getResult();
		Object ret = ((Map)JsonKit.json2Map(result).get("content")).get("result");
		AssertKit.assertEqual(ret, 3, null);
		System.out.println(mock.response.getResult());
	}

	/**
	 * 
	 */
	private static void testConcact() {
		HttpMock mock = new HttpMock();
		mock.request.setServletPath("/testremotepojo/concact.do");
//		mock.request.setPara("_o", "concact");
		mock.request.setPara("arg0", "abc");
		mock.request.setPara("arg1", "def");
		mock.invoke();
		String result = mock.response.getResult();
		Object ret = ((Map)JsonKit.json2Map(result).get("content")).get("result");
		AssertKit.assertEqual(ret, "abcdef", null);
		System.out.println(mock.response.getResult());
	}
	
	private static void testConcactJsonP() {
		HttpMock mock = new HttpMock();
		mock.request.setServletPath("/testremotepojo/concact.do");
//		mock.request.setPara("_o", "concact");
		mock.request.setPara("arg0", "abc");
		mock.request.setPara("arg1", "def");
		mock.request.setPara("callback", "function1");
		mock.invoke();
		String result = mock.response.getResult();
		AssertKit.assertTrue(result.startsWith("function1"));
		
		result = result.substring(10, result.length()-1);
		
		Object ret = ((Map)JsonKit.json2Map(result).get("content")).get("result");
		AssertKit.assertEqual(ret, "abcdef", null);
		System.out.println(mock.response.getResult());
	}

	/**
	 * 
	 */
	private static void test2() {
		HttpMock mock = new HttpMock();
		mock.request.setServletPath("/testremotepojo/nopara.do");
//		mock.request.setPara("_o", "nopara");
		mock.invoke();
		System.out.println(mock.response.getResult());
	}

	/**
	 * 
	 */
	private static void test1() {
		HttpMock mock;
		mock = new HttpMock();
		mock.request.setServletPath("/testremotepojo.nopara.do");
		mock.invoke();
		System.out.println(mock.response.getResult());
	}

}
