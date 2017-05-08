package net.jplugin.core.kernel.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.reso.ResolverKit;
import net.jplugin.core.kernel.Plugin;

public class PluginAutoDetect {

	private static Set<String> pkgPrepfixes = new HashSet();
	public static Collection<? extends Object> get(Set<Object> old) {
		addAutoDetectFromSysProperty();
		
		if (pkgPrepfixes.size()==0) return new HashSet();
		System.out.println("Auto Detect plugins from :"+pkgPrepfixes);
		return autoDetect(old);
	}
	
	private static void addAutoDetectFromSysProperty() {
		String property = System.getProperty("plugin-auto-detect");
		if (property==null) return;
		String[] path = StringKit.splitStr(property, ",");
		for (String p:path){
			addAutoDetectPackage(p);
		}
	}

	public static void addAutoDetectPackage(String pkg){
		pkgPrepfixes .add(pkg);
	}

	private static Collection<? extends Object> autoDetect(Set<Object> old) {
		if (pkgPrepfixes.isEmpty()) return new HashSet();
		ResolverKit rk = new ResolverKit();
		String[] arr = new String[pkgPrepfixes.size()];
		rk.findAnnotated(PluginAnnotation.class,pkgPrepfixes.toArray(arr) );
		Set clazz = rk.getClasses();
		Set ret = new HashSet();
		for (Object c:clazz){
			PluginAnnotation ann =  (PluginAnnotation) ((Class)c).getAnnotation(PluginAnnotation.class);
			if (ann==null) continue;
			if (ann.autoDetect()){
				String name = ((Class)c).getName();
				if (!old.contains(name))
					ret.add(name);
			}
		}
		System.out.println("Auto Detect plugins:" + ret);
		return ret;
	}

	

}
