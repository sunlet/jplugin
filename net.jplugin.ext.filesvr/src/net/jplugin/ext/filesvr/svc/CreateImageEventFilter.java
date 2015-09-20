package net.jplugin.ext.filesvr.svc;

import net.jplugin.core.event.api.Event;
import net.jplugin.core.event.api.IEventFilter;
import net.jplugin.ext.filesvr.api.FileCreatedEvent;
import net.jplugin.ext.filesvr.api.FileTypes;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-18 下午08:38:23
 **/

public class CreateImageEventFilter implements IEventFilter{

	/* (non-Javadoc)
	 * @see net.luis.plugin.event.api.IEventFilter#match(net.luis.plugin.event.api.Event)
	 */
	public boolean match(Event e) {
		FileCreatedEvent fce = (FileCreatedEvent) e;
		if (fce.getFile().getFileType() .equals( FileTypes.FT_IMAGE)){
			return true;
		}else{
			return false;
		}
	}

}
