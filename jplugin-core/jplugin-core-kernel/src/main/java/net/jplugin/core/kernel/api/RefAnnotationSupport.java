package net.jplugin.core.kernel.api;

public abstract class RefAnnotationSupport {
	protected RefAnnotationSupport(){
		PluginEnvirement.INSTANCE.resolveRefAnnotation(this);
	}
}
