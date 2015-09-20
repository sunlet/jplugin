package net.jplugin.ext.filesvr.db;

import net.jplugin.core.das.hib.api.IPersistObjDefinition;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-16 下午05:11:03
 **/

public class DBDefine implements IPersistObjDefinition {

	/* (non-Javadoc)
	 * @see net.luis.plugin.das.api.IPersistObjDefinition#getClasses()
	 */
	public Class[] getClasses() {
		return new Class[]{DBCloudFile.class};
	}

}
