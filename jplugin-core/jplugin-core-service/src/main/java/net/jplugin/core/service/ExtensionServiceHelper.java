package net.jplugin.core.service;

import java.lang.annotation.Annotation;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.extfactory.ObjectFactory;
import net.jplugin.core.kernel.kits.ExtensionBindKit;
import net.jplugin.core.service.api.BindService;
import net.jplugin.core.service.api.BindServiceSet;
import net.jplugin.core.service.api.Constants;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-6 下午03:31:14
 **/

public class ExtensionServiceHelper {

	/**
	 * 添加Service扩展，不指定名称
	 * @param plugin
	 * @param impl
	 */
	public static void addServiceExtension(AbstractPlugin plugin,Class impl){
		plugin.addExtension(Extension.create(Plugin.EP_SERVICE, impl));
	}

	/**
	 * 指定名称添加Sercie扩展
	 * @param plugin
	 * @param name
	 * @param impl
	 */
	public static void addServiceExtension(AbstractPlugin plugin,String name,Class impl){
		plugin.addExtension(Extension.create(Plugin.EP_SERVICE, name,impl));
	}

	/**
	 * interfaceName 作为name添加Service扩展
	 * @param plugin
	 * @param intf
	 * @param impl
	 */
	public static void addServiceExtension(AbstractPlugin plugin,Class intf,Class impl){
//		ObjectFactory f = ObjectFactory.createFactory(intf, impl);
		ObjectFactory f = ObjectFactory.createFactory(impl);
		plugin.addExtension(Extension.create(Plugin.EP_SERVICE, intf.getName(),f));
	}


	/**
	 * 本方法不推荐使用。 功能相同，请切换到 addBindExportService(...)
	 * Export a service
	 * @param plugin
	 * @param path
	 * @param beanClz
	 */
	@Deprecated
	public static void addServiceExportExtension(AbstractPlugin plugin,String path,Class beanClz){
		addExportServiceExtension(plugin, path, beanClz);
	}

	public static void addExportServiceExtension(AbstractPlugin plugin,String path,Class beanClz){
		ObjectFactory factory = ObjectFactory.createFactory(beanClz);
		plugin.addExtension(Extension.create(Plugin.EP_SERVICE_EXPORT, path, factory));
	}

	/**
	 * 自动遍历pkgPath子包下面的所有类，找到BindService 标注，自动注册为Service扩展。
	 * @param p
	 * @param pkgPath
	 */
	public static void autoBindServiceExtension(AbstractPlugin p, String pkgPath) {
		for(Class c:p.filterContainedClasses(pkgPath,BindService.class)){
			BindService anno = (BindService) c.getAnnotation(BindService.class);
			handleOneBind(p, c, anno);
		}
		
		for(Class<?> c:p.filterContainedClasses(pkgPath,BindServiceSet.class)){
			Annotation[] annos = c.getAnnotationsByType(BindService.class);
			for (Annotation a:annos){
				handleOneBind(p, c, a);
			}
		}
	}

	private static void handleOneBind(AbstractPlugin p, Class c, Annotation a) {
		BindService anno = (BindService) a;
//		Class interfaceClazz = anno.accessClass();
//		if (interfaceClazz.equals(BindService.class)){
//			interfaceClazz = c;
//		}

		String name = anno.name();
		if (StringKit.isNull(name)){
			addServiceExtension(p,c);
		}else{
			addServiceExtension(p,name,c);
		}

		PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Auto add extension for service: interface="
				+ " impl=" + c.getName());

		ExtensionBindKit.handleIdAndPriority(p,c);
	}

//2023-3-22备份
//	private static void handleOneBind(AbstractPlugin p, Class c, Annotation a) {
//		BindService anno = (BindService) a;
//		Class interfaceClazz = anno.accessClass();
//		if (interfaceClazz.equals(BindService.class)){
//			interfaceClazz = c;
//		}
//
//		addServiceExtension(p, interfaceClazz.getName(),c);
//		PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Auto add extension for service: interface="
//				+ interfaceClazz.getName() + " impl=" + c.getName());
//
//		ExtensionBindKit.handleIdAndPriority(p,c);
//	}
	
	private static Class computeInterfaceCls(Class impClazz) {
		Class[] clazzs = impClazz.getInterfaces();
		if (clazzs.length==0){
			return impClazz;
//			throw new RuntimeException("Class must implement a interface, so as to be defined as a Service impl. "+impClazz.getName());
		}
		if (clazzs.length>1){
			throw new RuntimeException("Class with multiple interfaces, must specify one interface in annotation. "+impClazz.getName());
		}
		return clazzs[0];
	}
}
