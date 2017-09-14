package net.jplugin.core.das.mybatis.api;

import net.jplugin.common.kits.reso.ResolverKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.mybatis.Plugin;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-1 下午09:01:41
 **/

public class ExtensionMybatisDasHelper {
	public static final String CONFIG_RES_PREFIX = "config://";

	public static void addMappingExtension(AbstractPlugin plugin,Class mappingIntf){
//		plugin.addExtension(Extension.create(Plugin.EP_MYBATIS_MAPPER, String.class,new String[][]{{"value",mappingIntf.getName()}}));
		addMappingExtension(plugin,DataSourceFactory.DATABASE_DSKEY,mappingIntf);
	}

	public static void addConfigExtension(AbstractPlugin plugin,String resource){
		addConfigExtension(plugin,DataSourceFactory.DATABASE_DSKEY, resource);
	}
	
	public static void addMappingExtension(AbstractPlugin plugin,String resource){
//		plugin.addExtension(Extension.create(Plugin.EP_MYBATIS_MAPPER, String.class,new String[][]{{"value",resource}}));
		addMappingExtension(plugin,DataSourceFactory.DATABASE_DSKEY, resource);
	}

	public static void addMappingExtension(AbstractPlugin plugin,String dataSource,Class mappingIntf){
//		plugin.addExtension(Extension.create(Plugin.EP_MYBATIS_MAPPER, dataSource,String.class,new String[][]{{"value",mappingIntf.getName()}}));
		addMappingExtension(plugin, dataSource,mappingIntf.getName());
	}
	
	public static void addConfigExtension(AbstractPlugin plugin,String dataSource,String resource){
		plugin.addExtension(Extension.create(Plugin.EP_MYBATIS_MAPPER, ExtensionDefinition4Mapping.class,new String[][]{{"dataSource",dataSource},{"interfOrResource",CONFIG_RES_PREFIX+resource}}));
	}
	
	public static void addMappingExtension(AbstractPlugin plugin,String dataSource,String resource){
		plugin.addExtension(Extension.create(Plugin.EP_MYBATIS_MAPPER, ExtensionDefinition4Mapping.class,new String[][]{{"dataSource",dataSource},{"interfOrResource",resource}}));
	}
	
	public static void addInctprorExtension(AbstractPlugin plugin,Class inceptorClass){
//		plugin.addExtension(Extension.create(Plugin.EP_MYBATIS_INCEPT, inceptorClass));
		addInctprorExtension(plugin,DataSourceFactory.DATABASE_DSKEY, inceptorClass);
	}
	
	public static void addInctprorExtension(AbstractPlugin plugin,String dataSource,Class inceptorClass){
		plugin.addExtension(Extension.create(Plugin.EP_MYBATIS_INCEPT,ExtensionDefinition4Incept.class, new String[][]{{"dataSource",dataSource},{"clazz",inceptorClass.getName()}}));
	}
	
	/**
	 * <PRE>
	 * 此方法自动遍历指定包下面的类，如果该类包含BindMapper 注解，则注册对应的Mapper扩展
	 * </PRE>
	 * @param p   对应的Plugin类
	 * @param pkgPath  相对于Plugin类的相对包路径。
	 */
	public static void autoBindMapperExtension(AbstractPlugin p, String pkgPath) {
		for (Class c : p.filterContainedClasses(pkgPath, BindMapper.class)) {
			BindMapper anno = (BindMapper) c.getAnnotation(BindMapper.class);
			
				ExtensionMybatisDasHelper.addMappingExtension(p, anno.dataSource(), c);
				PluginEnvirement.INSTANCE.getStartLogger()
						.log("$$$ Auto add extension for mybatis mapping : class=" + c);
		}
	}


	/**
	 * <PRE>
	 * 此方法自动遍历指定包下面的类，并且注册Mybatis Mapper接口。
	 * 相当于在Plugin构造函数中逐个调用 ExtensionMybatisDasHelper.addMappingExtension(p, clazz);
	 * </PRE>
	 * @param p   对应的Plugin类
	 * @param pkgPath  相对于Plugin类的相对包路径。
	 */
	@Deprecated
	public static void autoAddMappingExtension(AbstractPlugin p, String dataSource,String pkgPath) {
		String pkg = p.getClass().getPackage().getName() + pkgPath;
		ResolverKit kit = new ResolverKit<>();
		kit.find(pkg, (c) -> {
			ExtensionMybatisDasHelper.addMappingExtension(p,dataSource, c);
			PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Auto add extension for mybatis mapping : class=" + c);
			return false;
		});
	}
}
