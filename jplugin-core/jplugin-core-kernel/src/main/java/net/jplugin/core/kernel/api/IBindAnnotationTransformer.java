package net.jplugin.core.kernel.api;

import java.util.List;

public interface IBindAnnotationTransformer {
	public void transform(AbstractPlugin plugin, Class c, Object anno);
}
