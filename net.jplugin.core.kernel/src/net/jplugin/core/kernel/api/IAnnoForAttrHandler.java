package net.jplugin.core.kernel.api;

public interface IAnnoForAttrHandler {
	public Class getAttrClass();
	public Class getAnnoClass();
	public Object  getValue(Class fieldType,Object anno);
}
