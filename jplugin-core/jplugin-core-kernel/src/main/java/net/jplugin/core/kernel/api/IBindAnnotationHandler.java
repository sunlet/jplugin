package net.jplugin.core.kernel.api;

public interface IBindAnnotationHandler {
	public void transform(AbstractPlugin plugin, Class c, Object anno);
}
