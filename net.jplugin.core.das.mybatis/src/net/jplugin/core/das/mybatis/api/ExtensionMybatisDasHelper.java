package net.jplugin.core.das.mybatis.api;

import net.jplugin.core.das.mybatis.Plugin;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-1 下午09:01:41
 **/

public class ExtensionMybatisDasHelper {
	public static void addMappingExtension(AbstractPlugin plugin,Class mappingIntf){
		plugin.addExtension(Extension.create(Plugin.EP_MYBATIS_MAPPER, String.class,new String[][]{{"value",mappingIntf.getName()}}));
	}
}
