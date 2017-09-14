package net.jplugin.ext.webasic;

import net.jplugin.common.kits.reso.ResolverKit;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.api.AbstractExController;
import net.jplugin.ext.webasic.api.BindController;
import net.jplugin.ext.webasic.api.BindServiceExport;
import net.jplugin.ext.webasic.api.ObjectDefine;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 上午10:29:19
 **/

public class ExtensionWebHelper {
	//add rest method .   json 格式的远程方法
	/**
	 * Please call [addServiceExportExtension] instead.
	 * @param plugin
	 * @param path
	 * @param beanClz
	 */
	@Deprecated
	public static void addRestMethodExtension(AbstractPlugin plugin,String path,Class beanClz){
		addServiceExportExtension(plugin, path, beanClz);
	}
	
	/**
	 * Please call [addServiceExportExtension] instead.
	 * @param plugin
	 * @param path
	 * @param svcName
	 */
	@Deprecated
	public static void addRestMethodExtension(AbstractPlugin plugin,String path,String svcName){
		addServiceExportExtension(plugin, path, svcName);
	}
	
	/**
	 * Export a service
	 * @param plugin 
	 * @param path
	 * @param beanClz
	 */
	public static void addServiceExportExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	
	/**
	 * Export a service
	 * @param plugin
	 * @param path
	 * @param svcName
	 */
	public static void addServiceExportExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
	
//	public static void addRestMethodExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
//		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
//	}
//	public static void addRestMethodExtension(AbstractPlugin plugin,String path,String svcName,String method){
//		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
//	}

	//add rest service。Hashmap参数
	@Deprecated
	public static void addRestServiceExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	@Deprecated
	public static void addRestServiceExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
//	public static void addRestServiceExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
//		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
//	}
//	public static void addRestServiceExtension(AbstractPlugin plugin,String path,String svcName,String method){
//		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
//	}
	
	
	//add webex controller  扩展的webcontroller
	public static void addWebExControllerExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBEXCONTROLLER, path, ClassDefine.class,new String[][]{{"clazz",beanClz.getName()}} ));
	}
	
	//add web controller  Web控制
	public static void addWebControllerExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	public static void addWebControllerExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
//	public static void addWebControllerExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
//		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
//	}
//	public static void addWebControllerExtension(AbstractPlugin plugin,String path,String svcName,String method){
//		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
//	}

	//add remote call  Java序列化的远程服务
	@Deprecated
	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	@Deprecated
	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
//	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
//		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
//	}
//	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,String svcName,String method){
//		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
//	}
	
	//filter
	public static void addWebFilterExtension(AbstractPlugin plugin,Class filter){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBFILTER,"",filter));
	}
	//service filter
	public static void addServiceFilterExtension(AbstractPlugin plugin,Class sf){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_SERVICEFILTER,"",sf));
	}
	public static void addWebCtrlFilterExtension(AbstractPlugin plugin,Class sf){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCTRLFILTER,"",sf));
	}

	/**
	 * <PRE>
	 * 此方法自动遍历指定包下面的类，如果该类包含BindServiceExport 注解，则注册对应的ServiceExport扩展
	 * </PRE>
	 * @param p   对应的Plugin类
	 * @param pkgPath  相对于Plugin类的相对包路径，比如“.svc" ,可以为null
	 */
	public static void autoBindServiceExportExtension(AbstractPlugin p,String pkgPath) {
		for(Class c:p.filterContainedClasses(pkgPath,BindServiceExport.class)){
			BindServiceExport anno = (BindServiceExport) c.getAnnotation(BindServiceExport.class);
			addServiceExportExtension(p, anno.path(), c);
			PluginEnvirement.INSTANCE.getStartLogger()
			.log("$$$ Auto add extension for service export : servicePath=" + anno.path() + " class="
					+ c.getName());
		}
	}
	
	/**
	 * <PRE>
	 * 此方法自动遍历指定包下面的类，如果该类包含BindController 注解，则注册对应的WebController 或者 WebExController扩展
	 * </PRE>
	 * @param p   对应的Plugin类
	 * @param pkgPath  相对于Plugin类的相对包路径。
	 */
	public static void autoBindControllerExtension(AbstractPlugin p, String pkgPath) {
		for (Class c : p.filterContainedClasses(pkgPath, BindController.class)) {
			BindController anno = (BindController) c.getAnnotation(BindController.class);
			if (AbstractExController.class.isAssignableFrom(c)) {
				ExtensionWebHelper.addWebExControllerExtension(p, anno.path(), c);
				PluginEnvirement.INSTANCE.getStartLogger()
						.log("$$$ Auto add extension for web ex controller : servicePath=" + anno.path() + " class="
								+ c.getName());
			} else {
				ExtensionWebHelper.addWebControllerExtension(p, anno.path(), c);
				PluginEnvirement.INSTANCE.getStartLogger()
						.log("$$$ Auto add extension for web controller : servicePath=" + anno.path() + " class="
								+ c.getName());
			}
		}
	}
	
	/**
	 * <PRE>
	 * 此方法自动遍历指定包下面的类，并且发布为服务。
	 * 具体来说，对于 “Plugin类所属包的pkgPath子包” 下面的class，如果该Class以Export结尾，则自动注册为服务。
	 * 注册路径为 类名 除去"Export"后缀的部分。比如对于类 MyServiceExport，注册路径为 "/MyService"
	 * 可以在控制台或者系统启动日志中搜索“$$$ Auto add extension”查看相关自动注册情况。 
	 * 
	 * 比如：
	 * 		对于  MyServcieExport 类，自动注册到路径  /MyService。
	 *   	相当于执行代码 ExtensionWebHelper.addServiceExportExtension(this, "/MyService", MyServiceExport.class);
	 * </PRE>
	 * @param p   对应的Plugin类
	 * @param pkgPath  相对于Plugin类的相对包路径。
	 */
	@Deprecated
	public static void autoAddServiceExportExtension(AbstractPlugin p, String pkgPath) {
		String pkg = p.getClass().getPackage().getName() +pkgPath;
		ResolverKit kit = new ResolverKit<>();
		kit.find(pkg, (c) -> {
			if (c.getSimpleName().endsWith("Export")) {
				String classname = c.getSimpleName();
				String servicePath = "/" + classname.substring(0, classname.length() - "Export".length());
				ExtensionWebHelper.addServiceExportExtension(p, servicePath, c);
				PluginEnvirement.INSTANCE.getStartLogger()
						.log("$$$ Auto add extension for service export : servicePath=" + servicePath + " class="
								+ c.getName());
			}
			return false;
		});
	}

	/**
	 * <PRE>
	 * 此方法自动遍历指定包下面的类，并且发布为Web控制器。
	 * 对于 “Plugin类所属包的pkgPath子包” 下面class，如果以Controller结尾，则自动注册为Web控制器。
	 * 注册路径为 类名 除去 Controller后缀的部分。比如对于类 CustomerController，注册路径为 "/Customer"。
	 * 另外，在注册时，会根据是否继承AbstractExController来判断是调用addWebExControllerExtension，还是
	 * 调用addWebControllerExtension。
	 * 可以在控制台或者系统启动日志中搜索“$$$ Auto add extension”查看相关自动注册情况。 
	 * 
	 * 比如：
	 * 		对于  CustomerController 类，自动注册到路径  /Customer。
	 *      如果CustomerController继承了AbstractExController，相当于	执行代码 ExtensionWebHelper.addWebExControllerExtension(this, "/Customer", CustomerController.class);
	 *      如果CustomerController没有继承AbstractExController，相当于 执行代码 ExtensionWebHelper.addWebControllerExtension(this, "/Customer", CustomerController.class);
	 * </PRE>
	 * @param p   对应的Plugin类
	 * @param pkgPath  相对于Plugin类的相对包路径。
	 */
	@Deprecated
	public static void autoAddWebControllerExtension(AbstractPlugin p, String pkgPath) {
		String pkg = p.getClass().getPackage().getName() + pkgPath;
		ResolverKit kit = new ResolverKit<>();
		kit.find(pkg, (c) -> {
			if (c.getSimpleName().endsWith("Controller")){
				String classname = c.getSimpleName();
				String servicePath = "/" + classname.substring(0, classname.length() - "Controller".length());

				if (AbstractExController.class.isAssignableFrom(c)) {
										ExtensionWebHelper.addWebExControllerExtension(p, servicePath, c);
					PluginEnvirement.INSTANCE.getStartLogger()
							.log("$$$ Auto add extension for web ex controller : servicePath=" + servicePath + " class="
									+ c.getName());
				}else{
					ExtensionWebHelper.addWebControllerExtension(p, servicePath, c);
					PluginEnvirement.INSTANCE.getStartLogger()
							.log("$$$ Auto add extension for web controller : servicePath=" + servicePath + " class="
									+ c.getName());
				}
			}
			return false;
		});
	}
	
	public static void addHttpFilterExtension(AbstractPlugin p,Class c){
		p.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_HTTP_FILTER, c));
	}
	public static void addESFRpcFilterExtension(AbstractPlugin p,Class c){
		p.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_ESF_RPC_FILTER, c));
	}
	public static void addESFRestFilterExtension(AbstractPlugin p,Class c){
		p.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_ESF_REST_FILTER, c));
	}
}
