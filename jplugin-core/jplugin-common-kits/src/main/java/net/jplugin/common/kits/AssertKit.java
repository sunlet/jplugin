package net.jplugin.common.kits;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-16 下午04:58:41
 **/

public class AssertKit {

	public static void assertNotNull(Object o,String name){
		if (o==null) throw new RuntimeException(name +" shoudln't be null");
	}
	public static void assertStringNotNull(String s){
		assertStringNotNull(s,"");
	}
	public static void assertStringNotNull(String o,String name){
		if (StringKit.isNull(o)) throw new RuntimeException(name +" shoudln't be null");
	}
	/**
	 * @param type
	 * @param ftImage
	 */
	public static void assertEqual(Object a, Object b,String msg) {
		if (a == null){
			if ( b!=null){
				throw new RuntimeException("assert equal error 1."+msg+" expacted:"+a+" but:"+b);
			}
		}else{
			if (!a.equals(b)){
				throw new RuntimeException("assert equal error 1."+msg+" expacted:"+a+" but:"+b);
			}
		}
	}
	
	public static void assertNull(Object o) {
		assertNull(o,"Object");
	}
	/**
	 * @param entityId
	 */
	public static void assertNull(Object o,String name) {
		if (o!=null) throw new RuntimeException(name +" must be null");
	}
	/**
	 * @param runnable
	 */
	public static void assertException(Runnable runnable) {
		try{
			runnable.run();
		}catch(Throwable t){
			return;
		}
		throw new RuntimeException("assert failed,can't come here");
	}
	/**
	 * @param findObject
	 * @param string
	 */
	public static void assertEqual(Object a, Object b) {
		assertEqual(a, b,"");
	}
	/**
	 * @param null1
	 */
	public static void assertFalse(boolean b) {
		if (b) throw new RuntimeException("Assert error,must false");
	}
	/**
	 * @param containsKey
	 */
	public static void assertTrue(boolean b) {
		if (!b) throw new RuntimeException("Assert error,must true");
	}
	public static void assertTrue(boolean b, String string) {
		if (!b) throw new RuntimeException("Assert error,must true:"+string);
	}
	public static void assertStringNull(String s,String name) {
		if (StringKit.isNotNull(s)) throw new RuntimeException(name +" must be null");
	}
}
