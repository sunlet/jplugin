package net.jplugin.common.kits;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-9 下午08:48:01
 **/

public class ObjectRef<T> {
	T obj;
	public T get(){
		return obj;
	}
	public void set(T o){
		this.obj = o;
	}
}
