package net.jplugin.core.kernel.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PluginAnnotation {
	int prepareSeq();
}
