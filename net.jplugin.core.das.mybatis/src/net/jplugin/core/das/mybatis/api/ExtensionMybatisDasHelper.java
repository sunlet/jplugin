package net.jplugin.core.das.mybatis.api;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.mybatis.Plugin;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;

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
}
