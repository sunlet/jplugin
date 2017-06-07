package net.jplugin.ext.filesvr.api;

import net.jplugin.core.event.api.Event;
import net.jplugin.ext.filesvr.db.DBCloudFile;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-18 下午08:27:01
 **/

public class FileCreatedEvent extends Event{
	
	public static final String ET_FILE_CREATED = "FileCreated";
	public static final String IMAGE_EVENT_ALIAS = "ImgFileCreated";
	
	private DBCloudFile file;

	/**
	 * @param cf
	 */
	public FileCreatedEvent(DBCloudFile cf) {
		super(ET_FILE_CREATED);
		this.file = cf;
	}

	public DBCloudFile getFile() {
		return file;
	}


	
}
