package net.jplugin.ext.filesvr.svc;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.core.das.hib.api.IDataService;
import net.jplugin.core.event.api.Channel;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.filesvr.api.FileCreatedEvent;
import net.jplugin.ext.filesvr.db.DBCloudFile;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 下午03:14:49
 **/

public class FileService implements IFileService{

	/* (non-Javadoc)
	 * @see net.luis.plugin.filesvr.svc.IFileService#createFile(java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public long createFile(String filename, String fileType, long size,
			String storePath) {
		DBCloudFile cf = new DBCloudFile();
		AssertKit.assertStringNotNull(filename, "filename");
		AssertKit.assertStringNotNull(fileType, "fileType");
		AssertKit.assertStringNotNull(storePath, "storePath");
		
		cf.setFileName(filename);
		cf.setCreator(ThreadLocalContextManager.instance.getContext().getRequesterInfo().getOperatorId());
		cf.setExtName(FileKit.getFileExt(filename));
		cf.setFileName(filename);
		cf.setFileSize(size);
		cf.setFileType(fileType);
		cf.setStorePath(storePath);
		
		ServiceFactory.getService(IDataService.class).insert(cf);
		
		
		ServiceFactory.getService(Channel.class).sendEvent(new FileCreatedEvent(cf));
		
		return cf.getFileId();
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.filesvr.svc.IFileService#getFile()
	 */
	public DBCloudFile getFile(long fileid) {
		return ServiceFactory.getService(IDataService.class).findById(DBCloudFile.class, fileid);
	}

}
