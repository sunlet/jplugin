package net.jplugin.core.rclient.api;

import java.util.Hashtable;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-13 下午01:46:50
 **/

public class ClientFactory {
	public static <T> Client<T> create(Class<T> intf,String serverurl,ClientInfo ci){
		return new Client<T>(intf,serverurl,ci);
	}
	public static <T> Client<T> create(Class<T> intf,String serverurl){
		return new Client<T>(intf,serverurl,null);
	}
	/**
	 * 创建一个指定接口的Proxy
	 * @param intf
	 * @return
	 */
	public static <T> Client<T> create(Class<T> intf){
		return new Client<T>(intf,null,null);
	}
	
	static Hashtable<Class,ThreadLocal> threadLocalMap = new Hashtable();
	public static <T> Client<T> getThreadLocalClient(final Class<T> intf){
		ThreadLocal tl = threadLocalMap.get(intf);
		if (tl ==null){
			synchronized (ClientFactory.class) {
				tl = threadLocalMap.get(intf);
				if (tl ==null){
					tl = new ThreadLocal(){
						protected Object initialValue() {
							return new Client<T>(intf,null,null);
						};
					};		
					threadLocalMap.put(intf, tl);
				}
				return (Client<T>) tl.get();
			}
		}else{
			return (Client<T>) tl.get();
		}
	}

}
