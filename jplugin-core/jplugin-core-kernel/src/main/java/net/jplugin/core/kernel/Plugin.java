package net.jplugin.core.kernel;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.http.filter.IHttpClientFilter;
import net.jplugin.core.kernel.api.*;
import net.jplugin.core.kernel.impl.*;
import net.jplugin.core.kernel.impl_incept.ExtensionInterceptorManager;
import net.jplugin.core.kernel.kits.ExecutorKitFilterManager;
import net.jplugin.core.kernel.kits.ExtensionBindKit;
import net.jplugin.core.kernel.kits.RunnableInitFilterManager;
import net.jplugin.core.kernel.kits.scheduled.ScheduledFilterManager;

import java.lang.reflect.Modifier;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 下午01:07:22
 **/

public class Plugin extends AbstractPlugin{

	public static final String EP_STARTUP = "EP_STARTUP";
	public static final String EP_ANNO_FOR_ATTR = "EP_ANNO_FOR_ATTR";
	public static final String EP_EXECUTOR_FILTER = "EP_EXECUTOR_FILTER";
	public static final String EP_HTTP_CLIENT_FILTER = "EP_HTTP_CLIENT_FILTER";
	public static final String EP_EXE_RUN_INIT_FILTER = "EP_EXE_RUN_INIT_FILTER";
	public static final String EP_PLUGIN_ENV_INIT_FILTER = "EP_PLUGIN_ENV_INIT_FILTER";
	public static final String EP_EXE_SCHEDULED_FILTER = "EP_EXE_SCHEDULED_FILTER";
	public static final String EP_BEAN = "EP_BEAN";
	public static final String EP_EXTENSION_INTERCEPTOR = "EP_EXTENSION_INTERCEPTOR";

	static{
		//ExtensionPoint 也借用這個了！
		AutoBindExtensionManager.INSTANCE.addBindExtensionTransformer(BindExtensionPoint.class, (plugin, clazz, anno)->{
			if (clazz.isInterface() || Modifier.isAbstract( clazz.getModifiers() )){
				//OK
			}else{
				throw new RuntimeException("MakeExtensionPoint must use for Interface or Abstract class");
			}

			BindExtensionPoint a = (BindExtensionPoint) anno;
			String name = a.name();
			if (StringKit.isNull(a.name())){
				name = clazz.getName();
			}

			switch (a.type()){
				case LIST:
					if (((BindExtensionPoint) anno).supportPriority())
						plugin.addExtensionPoint(ExtensionPoint.createListWithPriority(name, clazz));
					else
						plugin.addExtensionPoint(ExtensionPoint.createList(name, clazz));
					break;
				case NAMED:
					if (((BindExtensionPoint) anno).supportPriority())
						plugin.addExtensionPoint(ExtensionPoint.createNamedWithPriority(name,clazz));
					else
						plugin.addExtensionPoint(ExtensionPoint.createNamed(name,clazz));
					break;
				case UNIQUE:
					if (((BindExtensionPoint) anno).supportPriority())
						plugin.addExtensionPoint(ExtensionPoint.createUniqueWithPriority(name,clazz));
					else
						plugin.addExtensionPoint(ExtensionPoint.createUnique(name,clazz));
					break;
				default:
					throw new RuntimeException("not support");

			}
		});

		AutoBindExtensionManager.INSTANCE.addSetAnnoTransformer(BindExtensionInterceptorSet.class,a->{
			BindExtensionInterceptorSet anno = (BindExtensionInterceptorSet) a;
			return anno.value();
		});

		AutoBindExtensionManager.INSTANCE.addBindExtensionTransformer(BindExtensionInterceptor.class, (plugin,clazz,anno)->{
			BindExtensionInterceptor bsAnno = (BindExtensionInterceptor) anno;
			ExtensionKernelHelper.addExtensionInterceptorExtension(plugin,clazz,((BindExtensionInterceptor) anno).forExtensions(),((BindExtensionInterceptor) anno).forExtensionPoints(),((BindExtensionInterceptor) anno).forImplClasses(),((BindExtensionInterceptor) anno).methodNameFilter(),((BindExtensionInterceptor) anno).methodAnnotationFilter());

			ExtensionBindKit.handleIdAndPriority(plugin,clazz);
		});

		AutoBindExtensionManager.INSTANCE.addBindExtensionHandler((p)->{
			ExtensionKernelHelper.autoBindExtension(p, "");
		});
		
		AutoBindExtensionManager.INSTANCE.addBindExtensionTransformer(BindStartup.class, (plugin,clazz,anno)->{
			BindStartup bsAnno = (BindStartup) anno;
			plugin.addExtension(Extension.create(EP_STARTUP, clazz));
			
			ExtensionBindKit.handleIdAndPriority(plugin,clazz);
		});
		AutoBindExtensionManager.INSTANCE.addBindExtensionTransformer(BindBean.class, (plugin,clazz,anno)->{
			BindBean bsAnno = (BindBean) anno;
			String id = bsAnno.id();
			if (StringKit.isNull(id)) {
				throw new RuntimeException("[id] attribute for BindBean must not null");
			}
			ExtensionKernelHelper.addBeanExtension(plugin, id, clazz);

			ExtensionBindKit.checkBindBeanNoSetExtensionIdAnno(plugin,clazz);
		});
	}
	
	public Plugin(){
		addExtensionPoint(ExtensionPoint.createListWithPriority(EP_STARTUP, IStartup.class));
		addExtensionPoint(ExtensionPoint.createList(EP_ANNO_FOR_ATTR, IAnnoForAttrHandler.class));
		addExtensionPoint(ExtensionPoint.createListWithPriority(EP_EXECUTOR_FILTER,IExecutorFilter.class));
		addExtensionPoint(ExtensionPoint.createListWithPriority(EP_EXE_RUN_INIT_FILTER,IExeRunnableInitFilter.class));
		addExtensionPoint(ExtensionPoint.createListWithPriority(EP_HTTP_CLIENT_FILTER,IHttpClientFilter.class));
		addExtensionPoint(ExtensionPoint.createListWithPriority(EP_PLUGIN_ENV_INIT_FILTER,IPluginEnvInitFilter.class));
		addExtensionPoint(ExtensionPoint.createListWithPriority(EP_EXE_SCHEDULED_FILTER,IScheduledExecutionFilter.class));
		addExtensionPoint(ExtensionPoint.createNamed(EP_BEAN, Object.class));
		addExtensionPoint(ExtensionPoint.createListWithPriority(EP_EXTENSION_INTERCEPTOR, AbstractExtensionInterceptor.class));
		
		ExtensionKernelHelper.addAnnoAttrHandlerExtension(this, AnnoForExtensionsHandler.class);
		ExtensionKernelHelper.addAnnoAttrHandlerExtension(this, AnnoForExtensionHandler.class);
		ExtensionKernelHelper.addAnnoAttrHandlerExtension(this, AnnoForExtensionMapHandler.class);
		ExtensionKernelHelper.addAnnoAttrHandlerExtension(this, AnnoForBeanHandler.class);

	}

	@Override
	public void afterPluginsContruct() {
		ExtensionInterceptorManager.setNeedIntercept();
	}

	@Override
	public void afterWire() {
		ExtensionInterceptorManager.setInterceptorsToFactories();
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.KERNEL;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void onCreateServices() {
		HttpClientFilterManager.init();
		ExecutorKitFilterManager.init();
		RunnableInitFilterManager.init();
		PluginEnvirement.getInstance().initStartFilter();
		ScheduledFilterManager.init();
	}
	
	@Override
	public void init() {
		
	}
	@Override
	public boolean searchClazzForExtension() {
		return false;
	}

}
