package net.jplugin.ext.webasic;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.impl.InitRequestInfoFilter;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 上午10:29:19
 **/

public class ExtensionWebHelper {
	//add rest method .   json 格式的远程方法
	public static void addRestMethodExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	public static void addRestMethodExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
	public static void addRestMethodExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
	}
	public static void addRestMethodExtension(AbstractPlugin plugin,String path,String svcName,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
	}

	//add rest service。Hashmap参数
	public static void addRestServiceExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	public static void addRestServiceExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
	public static void addRestServiceExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
	}
	public static void addRestServiceExtension(AbstractPlugin plugin,String path,String svcName,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
	}
	
	
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
	public static void addWebControllerExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
	}
	public static void addWebControllerExtension(AbstractPlugin plugin,String path,String svcName,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
	}

	//add remote call  Java序列化的远程服务
	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
	}
	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,String svcName,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
	}
	
	//filter
	public static void addWebFilterExtension(AbstractPlugin plugin,Class filter){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBFILTER,"",filter));
	}
}
