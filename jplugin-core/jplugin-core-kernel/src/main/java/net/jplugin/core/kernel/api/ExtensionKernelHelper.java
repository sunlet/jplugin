package net.jplugin.core.kernel.api;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.Plugin;

public class ExtensionKernelHelper {
	public static void addBeanExtension(AbstractPlugin p,String id,Class beanClazz){
		p.addExtension(Extension.create(net.jplugin.core.kernel.Plugin.EP_BEAN,id,beanClazz));
		Beans.setLastId(id);
	}
	public static void addStartUpExtension(AbstractPlugin p,Class startupClazz){
		p.addExtension(Extension.create(net.jplugin.core.kernel.Plugin.EP_STARTUP,"",startupClazz));
	}
	public static void addAnnoAttrHandlerExtension(AbstractPlugin plugin, Class class1) {
		plugin.addExtension(Extension.create(Plugin.EP_ANNO_FOR_ATTR, class1));
	}
	
	public static void addExecutorFilterExtension(AbstractPlugin plugin,Class c){
		plugin.addExtension(Extension.create(Plugin.EP_EXECUTOR_FILTER,c));
	}
	
	public static void addHttpClientFilterExtension(AbstractPlugin p,Class c){
		p.addExtension(Extension.create(Plugin.EP_HTTP_CLIENT_FILTER, c));
	}
	public static void addExeRunInitFilterExtension(AbstractPlugin p, Class c) {
		p.addExtension(Extension.create(Plugin.EP_EXE_RUN_INIT_FILTER, c));
	}
	public static void addPluginEnvInitFilterExtension(AbstractPlugin p, Class c) {
		p.addExtension(Extension.create(Plugin.EP_PLUGIN_ENV_INIT_FILTER, c));
	}
	
	public static void addScheduledExecutionFilterExtension(AbstractPlugin p, Class c) {
		p.addExtension(Extension.create(Plugin.EP_EXE_SCHEDULED_FILTER, c));
	}
	
	public static void autoBindExtension(AbstractPlugin p,String pkgPath){
		for(Class c:p.filterContainedClasses(pkgPath,BindExtension.class)){
			BindExtension anno = (BindExtension) c.getAnnotation(BindExtension.class);
			Extension ext = Extension.create(anno.pointTo(),anno.name(), c);
			ext.setPriority(anno.priority());
			p.addExtension(ext);
			PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Auto add extension for point:"+anno.pointTo()+" class="+c.getName()+" name="+anno.name());
			
			if (StringKit.isNotNull(anno.id())) {
				Beans.setLastId(anno.id());
			}
		}
	}
}
