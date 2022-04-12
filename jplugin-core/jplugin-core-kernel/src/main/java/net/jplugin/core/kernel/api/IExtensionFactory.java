package net.jplugin.core.kernel.api;

public  interface IExtensionFactory<T> {
	/**
	 *   创建一个扩展对象
	 * @return
	 */
	public T create();
}
