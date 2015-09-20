package net.jplugin.ext.filesvr.svc;

import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.api.Rule.TxType;
import net.jplugin.ext.filesvr.db.DBCloudFile;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-16 下午04:15:18
 **/

public interface IFileService {

	/**
	 * @param filename
	 * @param fileType
	 * @param size
	 * @param storePath
	 */
	@Rule(methodType=TxType.REQUIRED)
	long createFile(String filename, String fileType, long size,
			String storePath);

	/**
	 * 
	 */
	@Rule
	DBCloudFile getFile(long fileid);

}
