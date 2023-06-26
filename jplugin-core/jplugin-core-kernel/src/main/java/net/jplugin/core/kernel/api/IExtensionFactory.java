package net.jplugin.core.kernel.api;

public  interface IExtensionFactory {
	/**
	 *   创建一个扩展对象
	 * @return
	 */
	public Object create(Extension ext);

	public Class getImplClass();

	public boolean contentEqual(IExtensionFactory f);
}
