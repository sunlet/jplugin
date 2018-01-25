package net.luis.main.pojo;
import java.util.List;

import net.jplugin.core.das.hib.api.IPersistObjDefinition;
/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-18 下午05:28:51
 **/

public class POProvider implements IPersistObjDefinition{

	/* (non-Javadoc)
	 * @see net.luis.plugin.das.api.IPersistObjDefinition#getClasses()
	 */
	public Class[] getClasses() {
		return new Class[]{UserBean.class,BeanWithTypes.class};
	}

}
