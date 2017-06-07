package net.jplugin.core.das.hib.api;

import net.jplugin.core.das.hib.Plugin;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-1 下午09:01:41
 **/

public class ExtensionDasHelper {
	public static void addDataMappingExtension(AbstractPlugin plugin,Class entityCls){
		plugin.addExtension(Extension.create(Plugin.EP_DATAMAPPING_SINGLE, SinglePoDefine.class,new String[][]{{"poClass",entityCls.getName()}}));
	}
}
